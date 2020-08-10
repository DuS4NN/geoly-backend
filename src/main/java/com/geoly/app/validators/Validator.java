package com.geoly.app.validators;

import com.geoly.app.dao.EditQuest;
import com.geoly.app.models.QuestReview;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserOption;
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

    public ValidatorResponse getAllQuestByParametersValidator(List<Integer> difficulty, List<Integer> review){
        if (difficulty != null) {
            if (!validatorMethods.difficultyIsValid(difficulty))
                return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DIFFICULTY);
        }
        if (review != null) {
            if (!validatorMethods.reviewIsValid(review))
                return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW);
        }
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

    public ValidatorResponse removeReviewValidator(int questId, int reviewId){
        if(!validatorMethods.idIsValid(questId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.idIsValid(reviewId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse updateReviewValidator(int questId, QuestReview questReview){
        if(!validatorMethods.idIsValid(questId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
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
        if(!validatorMethods.imageTypeIsValid(type)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.UNSUPPORTED_IMAGE_TYPE);
        if(!validatorMethods.imageSizeIsValid(size)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.IMAGE_SIZE_TOO_BIG);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse changePasswordValidator(String password){
        if(!validatorMethods.passwordIsValid(password)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_PASSWORD);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse changeSettingsValidator(UserOption userOption, String about, int languageId){
        if(!validatorMethods.idIsValid(languageId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_LANGUAGE);
        if(!validatorMethods.idIsValid(userOption.getMapTheme())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_MAP_THEME);
        if(!validatorMethods.reviewTextFormatIsValid(about)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ABOUT);
        if(!validatorMethods.reviewTextLengthIsValid(about)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.ABOUT_TOO_LONG);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse editQuest(EditQuest questDetails, int questId){
        if(!validatorMethods.idIsValid(questId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.idIsValid(questDetails.getCategoryId())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
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

    public ValidatorResponse inviteUserValidator(int partyId, String nickName){
        if(!validatorMethods.idIsValid(partyId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.nickNameFormatIsValid(nickName)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_FORMAT);
        if(!validatorMethods.nickNameLengthIsValid(nickName)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME_LENGTH);

        return new ValidatorResponse(true);
    }
}
