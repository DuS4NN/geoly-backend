package com.geoly.app.validators;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidatorMethods {

    boolean idIsValid(int id) {
        return id > 0;
    }

    boolean difficultyIsValid(List<Integer> difficulty){
        return (difficulty.size() == 2 && difficulty.get(0) <= difficulty.get(1));
    }

    boolean reviewIsValid(List<Integer> review){
        return (review.size() == 2 && review.get(0) <= review.get(1));
    }

    boolean reviewTextFormatIsValid(String reviewText){
        return (reviewText != null && reviewText.length() > 0);
    }

    boolean reviewTextLengthIsValid(String reviewText){
        return reviewText.length() < 1001;
    }

    boolean reviewFormatIsValid(int review){
        return (review > 0 && review < 6);
    }

    boolean nickNameIsValid(String nickName){
        return (nickName.length() > 3 && nickName.length() < 16);
    }
}
