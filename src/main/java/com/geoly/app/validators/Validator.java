package com.geoly.app.validators;

import com.geoly.app.dao.*;
import com.geoly.app.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class Validator {

    private ValidatorMethods validatorMethods;

    public Validator(ValidatorMethods validatorMethods) {
        this.validatorMethods = validatorMethods;
    }

    public ValidatorResponse checkOnlyId(int id){
        if (!validatorMethods.idIsValid(id)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        return new ValidatorResponse(true);
    }

    public ValidatorResponse checkOnlyNameOfParty(String name){
        if (!validatorMethods.partyNameFormatIsValid(name)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_PARTY_NAME_FORMAT);
        if (!validatorMethods.partyNameLengthIsValid(name)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_PARTY_NAME_LENGTH);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse checkOnlyEmail(String email){
        if (!validatorMethods.emailIsValid(email)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_EMAIL);
        return new ValidatorResponse(true);
    }

    public ValidatorResponse checkOnlyPassword(String password){
        if (!validatorMethods.passwordIsValid(password)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_PASSWORD);
        return new ValidatorResponse(true);
    }

    public ValidatorResponse checkOnlyAddress(String address){
        if (!validatorMethods.addressIsValid(address)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ADDRESS);
        return new ValidatorResponse(true);
    }

    public ValidatorResponse getAllQuestByParametersInRadius(QuestSearch questSearch){
        if(questSearch.getCategoryId() == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_CATEGORY);
        if(questSearch.getStageType() == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_STAGE_TYPE);
        if(questSearch.getDifficulty() == null || !validatorMethods.difficultyArrayIsValid(questSearch.getDifficulty())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DIFFICULTY);
        if(questSearch.getReview() == null || !validatorMethods.reviewArrayIsValid(questSearch.getReview())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW);
        if(questSearch.getCoordinatesNw() == null || !validatorMethods.coordinatesArrayIsValid(questSearch.getCoordinatesNw())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_COORDINATES);
        if(questSearch.getCoordinatesSe() == null || !validatorMethods.coordinatesArrayIsValid(questSearch.getCoordinatesSe())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_COORDINATES);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse createReviewValidator(int questId, QuestReview questReview){
        if(!validatorMethods.idIsValid(questId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(questReview == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW_FORMAT);
        if(!validatorMethods.reviewTextFormatIsValid(questReview.getReviewText())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW_TEXT_FORMAT);
        if(!validatorMethods.reviewTextLengthIsValid(questReview.getReviewText())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW_LENGTH_SIZE);
        if(!validatorMethods.reviewFormatIsValid(questReview.getReview())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse removeReviewValidator( int reviewId){
         if(!validatorMethods.idIsValid(reviewId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse updateReviewValidator(QuestReview questReview){
        if(!validatorMethods.idIsValid(questReview.getId())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.reviewFormatIsValid(questReview.getReview())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW);
        if(!validatorMethods.reviewTextFormatIsValid(questReview.getReviewText())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW_TEXT_FORMAT);
        if(!validatorMethods.reviewTextLengthIsValid(questReview.getReviewText())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW_LENGTH_SIZE);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse getProfileValidator(String nickName){
        if(!validatorMethods.nickNameFormatIsValid(nickName)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_FORMAT);
        if(!validatorMethods.nickNameLengthIsValid(nickName)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_LENGTH);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse registerValidator(User user, int languageId){
        if(user.getNickName() == null || user.getEmail() == null || user.getPassword() == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.USER_INFO_IS_NULL);
        if(!validatorMethods.nickNameFormatIsValid(user.getNickName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_FORMAT);
        if(!validatorMethods.nickNameLengthIsValid(user.getNickName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_LENGTH);
        if(!validatorMethods.emailIsValid(user.getEmail())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_EMAIL);
        if(!validatorMethods.passwordIsValid(user.getPassword())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_PASSWORD);
        if(!validatorMethods.idIsValid(languageId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_LANGUAGE);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse imageValidator(String type, long size){
        if(!validatorMethods.imageTypeIsValid(type)) return new ValidatorResponse(false, HttpStatus.METHOD_NOT_ALLOWED, StatusMessage.UNSUPPORTED_IMAGE_TYPE);
        if(!validatorMethods.imageSizeIsValid(size)) return new ValidatorResponse(false, HttpStatus.METHOD_NOT_ALLOWED, StatusMessage.IMAGE_SIZE_TOO_BIG);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse changePasswordValidator(String password){
        if(!validatorMethods.passwordIsValid(password)) return new ValidatorResponse(false, HttpStatus.METHOD_NOT_ALLOWED, StatusMessage.INVALID_PASSWORD);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse changeSettingsValidator(Settings settings){
        if(!validatorMethods.idIsValid(settings.getLanguageId())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_LANGUAGE);
        if(!validatorMethods.reviewTextFormatIsValid(settings.getAbout())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ABOUT);
        if(!validatorMethods.reviewTextLengthIsValid(settings.getAbout())) return new ValidatorResponse(false, HttpStatus.METHOD_NOT_ALLOWED, StatusMessage.INVALID_ABOUT_LENGTH_SIZE);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse editQuest(EditQuest questDetails, int questId){
        if(!validatorMethods.idIsValid(questId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.idIsValid(questDetails.getCategoryId())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.nameIsValid(questDetails.getName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NAME_LENGTH_SIZE);
        if(!validatorMethods.difficultyIsValidInt(questDetails.getDifficulty())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DIFFICULTY);
        if(questDetails.getDescription() != null && !validatorMethods.descriptionIsValid(questDetails.getDescription())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DESCRIPTION);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse imagesValidator(List<MultipartFile> files, int questId){
        if(!validatorMethods.idIsValid(questId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.imageCountIsValid(files.size())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.TOO_MANY_IMAGES);
        for(MultipartFile file : files){
            if(!validatorMethods.imageSizeIsValid(file.getSize())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.IMAGE_SIZE_TOO_BIG);
            if(!validatorMethods.imageTypeIsValid(file.getContentType())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.UNSUPPORTED_IMAGE_TYPE);
        }

        return new ValidatorResponse(true);
    }

    public ValidatorResponse editStage(EditStage editStage){
        if(!validatorMethods.isStageTextValid(editStage.getQuestion())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_STAGE_TEXT);
        if(!validatorMethods.isStageTextValid(editStage.getAnswer())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_STAGE_TEXT);

        if(editStage.getAdvise() != null && editStage.getNote() != null){
            if(!validatorMethods.isStageTextValid(editStage.getAdvise())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_STAGE_TEXT);
            if(!validatorMethods.isStageTextValid(editStage.getNote())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_STAGE_TEXT);
        }

        if(editStage.getAnswerList() != null){
            if(editStage.getAnswerList().length() > 1000) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_STAGE_TEXT);
            if(editStage.getAnswerList().split(";").length > 5) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ANSWER_COUNT);
        }

        return new ValidatorResponse(true);
    }

    public ValidatorResponse editUser(AdminEditUser adminEditUser){
        if(!validatorMethods.nickNameFormatIsValid(adminEditUser.getNickName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_FORMAT);
        if(!validatorMethods.nickNameLengthIsValid(adminEditUser.getNickName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_LENGTH);

        if (!validatorMethods.emailIsValid(adminEditUser.getEmail())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_EMAIL);

        if(!validatorMethods.addressIsValid(adminEditUser.getAddress())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ADDRESS);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse adminEditQuest(AdminEditQuest adminEditQuest){
        if(!validatorMethods.idIsValid(adminEditQuest.getCategory())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.difficultyIsValidInt(adminEditQuest.getDifficulty())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DIFFICULTY);
        if(!validatorMethods.nameIsValid(adminEditQuest.getName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NAME_LENGTH_SIZE);
        if(adminEditQuest.getDescription() != null && !validatorMethods.descriptionIsValid(adminEditQuest.getDescription())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DESCRIPTION);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse addQuest(AddQuest addQuest){
        if(!validatorMethods.difficultyIsValidInt(addQuest.getDifficulty())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DIFFICULTY);
        if(!validatorMethods.idIsValid(addQuest.getCategoryId())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.CATEGORY_NOT_FOUND);
        if(!validatorMethods.nameIsValid(addQuest.getName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NAME_LENGTH_SIZE);
        if(!validatorMethods.descriptionIsValid(addQuest.getDescription())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DESCRIPTION);

        for(Stage stage : addQuest.getStages()){
            switch (stage.getType()){
                case GO_TO_PLACE:
                    if(stage.getLatitude() == null || stage.getLongitude() == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_COORDINATES);
                    break;
                case SCAN_QR_CODE:
                    break;
                case ANSWER_QUESTION:
                    if(!validatorMethods.stageRequiredTextIsValid(stage.getQuestion())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_QUESTION);
                    if(!validatorMethods.stageRequiredTextIsValid(stage.getAnswer())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ANSWER);

                    if(!validatorMethods.stageNonRequiredTextIsValid(stage.getAdvise())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ADVISE);
                    if(!validatorMethods.stageNonRequiredTextIsValid(stage.getNote())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NOTE);
                    if(!validatorMethods.answersListIsValid(stage.getAnswersList())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ANSWERS_LIST);
                    break;
            }
        }

        return new ValidatorResponse(true);
    }
}
