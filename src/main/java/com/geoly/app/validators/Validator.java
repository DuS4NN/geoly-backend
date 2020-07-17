package com.geoly.app.validators;

import com.geoly.app.models.StatusMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Validator {

    private ValidatorMethods validatorMethods;

    public Validator(ValidatorMethods validatorMethods){
        this.validatorMethods = validatorMethods;
    }

    public ValidatorResponse getQuestDetailsByIdValidator(int id){
        if(!validatorMethods.idIsValid(id)){
            System.out.println(validatorMethods.idIsValid(id));
            return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_ID);
        }
        return new ValidatorResponse(true);
    }

    public ValidatorResponse getAllQuestByParametersValidator(List<Integer> difficulty, List<Integer> review){
        if(difficulty != null){
            if(!validatorMethods.difficultyIsValid(difficulty)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_DIFFICULTY);
        }
        if(review != null){
            if(!validatorMethods.reviewIsValid(review)) return new ValidatorResponse(false, HttpStatus.BAD_REQUEST, StatusMessage.INVALID_REVIEW);
        }
        return new ValidatorResponse(true);
    }
}
