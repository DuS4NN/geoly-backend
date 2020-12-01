package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.AddQuest;
import com.geoly.app.dao.AdminEditQuest;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.jooq.tables.UserQuest;
import com.geoly.app.models.*;
import com.geoly.app.repositories.CategoryRepository;
import com.geoly.app.repositories.ImageRepository;
import com.geoly.app.repositories.QuestRepository;
import com.geoly.app.repositories.UserRepository;
import com.google.common.hash.Hashing;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.tinify.Tinify;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static org.jooq.impl.DSL.count;

@Service
public class AdminQuestService {

    private EntityManager entityManager;
    private DSLContext create;
    private QuestRepository questRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ImageRepository imageRepository;
    private API api;
    private CloudBlobContainer cloudBlobContainer;

    public AdminQuestService(EntityManager entityManager, DSLContext create, QuestRepository questRepository, UserRepository userRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, API api, CloudBlobContainer cloudBlobContainer) {
        this.entityManager = entityManager;
        this.create = create;
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.api = api;
        this.cloudBlobContainer = cloudBlobContainer;
    }

    public Response getQuests(String name, int page){

        Condition condition = DSL.trueCondition();
        if(!name.equals("")){
            condition = condition.and(Quest.QUEST.NAME.like("%"+name+"%"));
        }

        Select<?> count =
            create.select(count())
                .from(Quest.QUEST)
                .where(condition);

        Query q1 = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q1, count);
        Object countResult = q1.getSingleResult();

        Select<?> query =
            create.select(Quest.QUEST.NAME, Quest.QUEST.ID, Quest.QUEST.CREATED_AT)
                .from(Quest.QUEST)
                .where(condition)
                .orderBy(Quest.QUEST.CREATED_AT.desc())
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List queryResult = q.getResultList();

        List response = new ArrayList();
        response.add(Integer.parseInt(String.valueOf(countResult)));
        response.add(queryResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public Response getQuestDetails(int id){
        Select<?> details =
            create.select(Quest.QUEST.CREATED_AT, Quest.QUEST.NAME, Quest.QUEST.ACTIVE, Quest.QUEST.PREMIUM, Quest.QUEST.PRIVATE_QUEST, Quest.QUEST.DIFFICULTY, Quest.QUEST.DESCRIPTION, Quest.QUEST.CATEGORY_ID, Quest.QUEST.USER_ID)
                .from(Quest.QUEST)
                .where(Quest.QUEST.ID.eq(id));

        Query q1 = entityManager.createNativeQuery(details.getSQL());
        API.setBindParameterValues(q1, details);
        List detailsResult = q1.getResultList();

        Select<?> stages =
            create.select(Stage.STAGE.ANSWERS_LIST, Stage.STAGE.ADVISE, Stage.STAGE.ANSWER, Stage.STAGE.TYPE, Stage.STAGE.QUESTION, Stage.STAGE.QR_CODE_URL, Stage.STAGE.LONGITUDE, Stage.STAGE.LATITUDE, Stage.STAGE.ID, Stage.STAGE.NOTE)
                .from(Stage.STAGE)
                .where(Stage.STAGE.QUEST_ID.eq(id))
                .orderBy(Stage.STAGE.ID.desc());

        Query q2 = entityManager.createNativeQuery(stages.getSQL());
        API.setBindParameterValues(q2, stages);
        List stagesResult = q2.getResultList();

        Select<?> images =
            create.select(com.geoly.app.jooq.tables.Image.IMAGE.ID, com.geoly.app.jooq.tables.Image.IMAGE.IMAGE_URL)
                .from(com.geoly.app.jooq.tables.Image.IMAGE)
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(com.geoly.app.jooq.tables.Image.IMAGE.QUEST_ID))
                .where(Quest.QUEST.ID.eq(id));

        Query q3 = entityManager.createNativeQuery(images.getSQL());
        API.setBindParameterValues(q3, images);
        List imagesResult = q3.getResultList();

        List<List> result = new ArrayList<>();
        result.add(detailsResult);
        result.add(stagesResult);
        result.add(imagesResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getQuestPlayed(int id, int page, int userId){

        Condition condition = DSL.trueCondition();
        if(userId != 0){
            condition = condition.and(UserQuest.USER_QUEST.USER_ID.eq(userId));
        }


        Select<?> count =
            create.select(count())
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                    .where(Stage.STAGE.QUEST_ID.eq(id))
                    .and(condition);

        Query q1 = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q1, count);
        Object countResult = q1.getSingleResult();

        Select<?> query =
            create.select(UserQuest.USER_QUEST.ID.as("userQuestId"), UserQuest.USER_QUEST.UPDATED_AT, UserQuest.USER_QUEST.STATUS, Stage.STAGE.ID.as("stageId"), Stage.STAGE.TYPE, User.USER.NICK_NAME, User.USER.ID.as("userId"), User.USER.PROFILE_IMAGE_URL, UserQuest.USER_QUEST.CREATED_AT)
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(UserQuest.USER_QUEST.USER_ID))
                .where(Stage.STAGE.QUEST_ID.eq(id))
                .and(condition)
                .orderBy(UserQuest.USER_QUEST.CREATED_AT.desc())
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List resultList = q.getResultList();


        List result = new ArrayList();
        result.add(Integer.parseInt(String.valueOf(countResult)));
        result.add(resultList);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response editQuest(AdminEditQuest adminEditQuest, int adminId){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(adminEditQuest.getId());
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<Category> category = categoryRepository.findById(adminEditQuest.getCategory());
        if(!category.isPresent()) return new Response(StatusMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        quest.get().setName(adminEditQuest.getName());
        quest.get().setDescription(adminEditQuest.getDescription());
        quest.get().setDifficulty(adminEditQuest.getDifficulty());
        quest.get().setActive(adminEditQuest.isActive());
        quest.get().setPrivateQuest(adminEditQuest.isPrivateQuest());
        quest.get().setPremium(adminEditQuest.isPremium());
        quest.get().setCategory(category.get());

        Log log = new Log();
        log.setLogType(LogType.QUEST_UPDATE);

        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("editObject", adminEditQuest.toString());
        log.setData(jo.toString());

        entityManager.persist(log);
        entityManager.merge(quest.get());

        return new Response(StatusMessage.QUEST_EDITED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response addQuest(AddQuest addQuest, int adminId) throws Exception{

        Optional<com.geoly.app.models.User> user = userRepository.findByEmail("info@geoly.com");
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<Category> category = categoryRepository.findById(addQuest.getCategoryId());
        if(!category.isPresent()) return new Response(StatusMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        com.geoly.app.models.Quest quest = new com.geoly.app.models.Quest();
        quest.setCategory(category.get());
        quest.setPremium(addQuest.isPremium());
        quest.setPrivateQuest(addQuest.isPrivateQuest());
        quest.setActive(addQuest.isActive());
        quest.setUser(user.get());
        quest.setDifficulty(addQuest.getDifficulty());
        quest.setDescription(addQuest.getDescription());
        quest.setName(addQuest.getName());
        quest.setDaily(false);
        entityManager.persist(quest);

        for(com.geoly.app.models.Stage stage : addQuest.getStages()){
            stage.setQuest(quest);
            if(stage.getType() == StageType.SCAN_QR_CODE){
                long imageName = System.currentTimeMillis();
                byte[] qrCode = generateQrCode();
                String url = api.uploadImage(API.qrCodeImageUrl+"/"+quest.getId()+"/"+imageName+".jpg", qrCode);
                stage.setQrCodeUrl(url);
            }
            entityManager.persist(stage);
        }

        Log log = new Log();
        log.setLogType(LogType.ADD_QUEST);

        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("questId", quest.getId());
        log.setData(jo.toString());

        entityManager.persist(log);

        List result = new ArrayList();
        result.add(quest.getId());

        return new Response(StatusMessage.QUEST_CREATED, HttpStatus.ACCEPTED, result);
    }

    private byte[] generateQrCode() throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        Random rand = new Random();
        BitMatrix matrix = new MultiFormatWriter().encode(new String(Hashing.sha256().hashString(""+rand.nextInt(999999), Charset.defaultCharset()).asBytes(), "UTF-8"), BarcodeFormat.QR_CODE, 500, 500);

        int height = matrix.getHeight();
        int width = matrix.getWidth();

        MatrixToImageConfig config = new MatrixToImageConfig();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int onColor = config.getPixelOnColor();
        int offColor = config.getPixelOffColor();

        int[] rowPixels = new int[width];
        BitArray row = new BitArray(width);
        for (int y = 0; y < height; y++) {
            row = matrix.getRow(y, row);
            for (int x = 0; x < width; x++) {
                rowPixels[x] = row.get(x) ? onColor : offColor;
            }
            image.setRGB(0, y, width, 1, rowPixels, 0, width);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    @Transactional(rollbackOn = Exception.class)
    public Response updateImages(List<MultipartFile> files, int adminId, int questId, int[] deleted) throws Exception{

        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        for(int deletedImageId : deleted){
            Optional<Image> image = imageRepository.findByIdAndQuest(deletedImageId, quest.get());
            if(image.isPresent()){
                cloudBlobContainer.getBlockBlobReference(image.get().getImageUrl().replace("/images/", "")).delete();
                entityManager.remove(image.get());
            }
        }

        for(MultipartFile file : files){

            long imageName = System.currentTimeMillis();

            byte[] resultData = Tinify.fromBuffer(file.getBytes()).toBuffer();
            String url = api.uploadImage(API.questImageUrl+questId+"/"+imageName+".jpg", resultData);

            Image image = new Image();
            image.setQuest(quest.get());
            image.setImageUrl(url);
            entityManager.persist(image);
        }

        Log log = new Log();
        log.setLogType(LogType.UPDATE_IMAGES);
        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("questId", questId);
        log.setData(jo.toString());
        entityManager.persist(log);

        return new Response(StatusMessage.IMAGES_SAVED, HttpStatus.ACCEPTED, null);
    }
}
