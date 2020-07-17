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
}
