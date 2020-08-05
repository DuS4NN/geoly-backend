package com.geoly.app.listeners;

import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.Premium;
import com.geoly.app.models.User;
import com.geoly.app.repositories.PremiumRepository;
import com.geoly.app.repositories.UserRepository;
import com.paypal.base.rest.APIContext;
import io.sentry.Sentry;
import org.json.JSONObject;
import org.springframework.context.ApplicationListener;

import org.springframework.http.*;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private EntityManager entityManager;
    private UserRepository userRepository;
    private PremiumRepository premiumRepository;
    private APIContext context;

    public LoginListener(EntityManager entityManager, UserRepository userRepository, PremiumRepository premiumRepository, APIContext context) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.premiumRepository = premiumRepository;
        this.context = context;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        try{
            CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication().getPrincipal();

            Optional<User> user = userRepository.findById(userDetails.getUser().getId());
            if(!user.isPresent()) return;

            Optional<Premium> premium = premiumRepository.findByUserAndState(user.get(), "Active");
            if(!premium.isPresent()) return;

            if(premium.get().getEndAt().after(new Date())) return;

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBasicAuth(context.getClientID(), context.getClientSecret());

            String url = "https://api.sandbox.paypal.com/v1/payments/billing-agreements/"+ premium.get().getAgreementId();
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

            JSONObject jsonObject = new JSONObject(response.getBody());
            String state = jsonObject.get("state").toString();

            if(!state.equals("Active")){
                premium.get().setState(state);
                entityManager.merge(premium.get());
            }
        }catch (Exception e){
            Sentry.capture(e);
        }
    }
}