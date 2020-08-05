package com.geoly.app.services;

import com.geoly.app.models.Premium;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.repositories.PremiumRepository;
import com.geoly.app.repositories.UserRepository;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.jooq.DSLContext;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PremiumService {

    private APIContext context;
    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private PremiumRepository premiumRepository;

    public PremiumService(APIContext context, EntityManager entityManager, DSLContext create, UserRepository userRepository, PremiumRepository premiumRepository) {
        this.context = context;
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.premiumRepository = premiumRepository;
    }

    public String createSubscription(int userId) throws PayPalRESTException{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return StatusMessage.USER_NOT_FOUND.toString();

        Optional<Premium> premium = premiumRepository.findByUserAndState(user.get(), "Active");
        if(premium.isPresent()) return StatusMessage.USER_ALREADY_HAS_PREMIUM.toString();

        String successUrl = "http://localhost:8080/premium/success";
        String cancelUrl = "http://localhost:8080/premium/cancel";

        Plan plan = new Plan();
        plan.setName("Geoly Subscription");
        plan.setType("fixed");
        plan.setDescription("Geoly Subscription");

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Geoly Payments");
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency("MONTH");
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles("12");

        Currency currency = new Currency();
        currency.setCurrency("USD");
        currency.setValue("2.90");
        paymentDefinition.setAmount(currency);

        List<PaymentDefinition> paymentDefinitions = new ArrayList<>();
        paymentDefinitions.add(paymentDefinition);
        plan.setPaymentDefinitions(paymentDefinitions);

        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setSetupFee(currency);
        merchantPreferences.setCancelUrl(cancelUrl);
        merchantPreferences.setReturnUrl(successUrl);
        merchantPreferences.setSetupFee(currency);
        merchantPreferences.setMaxFailAttempts("0");
        merchantPreferences.setAutoBillAmount("NO");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        plan.setMerchantPreferences(merchantPreferences);

        Plan createdPlan = plan.create(context);

        List<Patch> patchRequestList = new ArrayList<>();
        Map<String, String> value = new HashMap<>();
        value.put("state", "ACTIVE");

        Patch patch = new Patch();
        patch.setPath("/");
        patch.setValue(value);
        patch.setOp("replace");
        patchRequestList.add(patch);

        createdPlan.update(context, patchRequestList);
        return createdPlan.getId();
    }

    public String createAgreement(String planId) throws Exception{
        Agreement agreement = new Agreement();
        agreement.setName("Geoly Agreement");
        agreement.setDescription("Geoly Agreement");


        Calendar current = Calendar.getInstance();
        current.add(Calendar.DAY_OF_YEAR, 30);

        Date date = new Date(current.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        agreement.setStartDate(sdf.format(date));

        Plan plan = new Plan();
        plan.setId(planId);
        agreement.setPlan(plan);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        agreement.setPayer(payer);

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setLine1("Cabaj");
        shippingAddress.setCity("Cabaj - Capor");
        shippingAddress.setState("Slovakia");
        shippingAddress.setCountryCode("SK");
        shippingAddress.setPostalCode("95117");
        agreement.setShippingAddress(shippingAddress);

        agreement = agreement.create(context);
        for(Links links : agreement.getLinks()){
            if(links.getRel().equals("approval_url")){
                return links.getHref();
            }
        }

        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    public List executeAgreement(String token, int userId) throws PayPalRESTException, ParseException {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Agreement agreement = new Agreement();
        agreement.setToken(token);
        Agreement activeAgreement = agreement.execute(context, agreement.getToken());

        if(!activeAgreement.getState().equals("Active")){
            return Collections.singletonList(new ResponseEntity<>(StatusMessage.PAYMENT_WAS_CANCELED, HttpStatus.NOT_ACCEPTABLE));
        }

        Premium premium = new Premium();
        premium.setAgreementId(activeAgreement.getId());
        premium.setState(activeAgreement.getState());
        premium.setStartAt(new Date());
        premium.setEndAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(activeAgreement.getAgreementDetails().getNextBillingDate()));
        premium.setUser(user.get());
        entityManager.persist(premium);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.SIGNED_UP_PREMIUM, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List cancelAgreement(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<Premium> premium = premiumRepository.findByUserAndState(user.get(), "Active");
        if(!premium.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_DOESNT_HAVE_PREMIUM, HttpStatus.BAD_REQUEST));

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(context.getClientID(), context.getClientSecret());

        String jsonInputString = "{\"reason\": \"Cancel Geoly Subscription\"}";
        String url = "https://api.sandbox.paypal.com/v1/billing/subscriptions/"+ premium.get().getAgreementId() +"/cancel";

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonInputString, httpHeaders);

        String answer = restTemplate.postForObject(url, httpEntity, String.class);

        if(answer == null){
            premium.get().setState("Cancelled");
            entityManager.merge(premium.get());
            return Collections.singletonList(new ResponseEntity<>(StatusMessage.SUBSCRIPTION_CANCELED, HttpStatus.METHOD_NOT_ALLOWED));
        }else{
            return Collections.singletonList(new ResponseEntity<>(StatusMessage.SUBSCRIPTION_CAN_NOT_BE_CANCELED, HttpStatus.METHOD_NOT_ALLOWED));
        }
    }
}