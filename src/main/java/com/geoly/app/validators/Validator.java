package com.geoly.app.validators;

import com.geoly.app.models.QuestReview;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserOption;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
        if(!validatorMethods.nickNameIsValid(nickName)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse registerValidator(User user, int languageId){
        if(user.getNickName() == null || user.getEmail() == null || user.getPassword() == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.USER_INFO_IS_NULL);
        if(!validatorMethods.nickNameIsValid(user.getNickName())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_NICKNAME);
        if(!validatorMethods.emailIsValid(user.getEmail())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_EMAIL);
        if(!validatorMethods.passwordIsValid(user.getPassword())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_PASSWORD);
        if(!validatorMethods.idIsValid(languageId)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_LANGUAGE);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse setOptionsValidator(UserOption userOption, User user){
        if(user.getAbout() == null || user.getProfileImageUrl() == null || userOption.getLanguage() == null) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_SETTINGS);
        if(!validatorMethods.idIsValid(userOption.getMapTheme())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        if(!validatorMethods.aboutIsValid(user.getAbout())) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ABOUT);

        return new ValidatorResponse(true);
    }

    public ValidatorResponse imageValidator(String type, long size){
        if(!validatorMethods.imageTypeIsValid(type)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.UNSUPPORTED_IMAGE_TYPE);
        if(!validatorMethods.imageSizeIsValid(size)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.IMAGE_SIZE_TOO_BIG);

        return new ValidatorResponse(true);
    }
}
