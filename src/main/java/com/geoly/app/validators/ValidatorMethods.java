package com.geoly.app.validators;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorMethods {

    boolean idIsValid(int id) {
        return id > 0;
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

    boolean nickNameLengthIsValid(String nickName){
       return nickName.length() > 3 && nickName.length() < 16;
    }

    boolean nickNameFormatIsValid(String nickName){
        Pattern r = Pattern.compile("[A-Z,a-z,\\-,_,.,0-9]*");
        Matcher m = r.matcher(nickName);
        return m.matches();
    }

    boolean passwordIsValid(String password){
        return (password.length() > 3 && password.length() < 21);
    }

    boolean emailIsValid(String email){
        Pattern r = Pattern.compile(".+@.+\\..+");
        Matcher m = r.matcher(email);
        return m.matches();
    }

    boolean aboutIsValid(String about){
        return (about.length() > 0 && about.length() < 1000);
    }

    boolean imageTypeIsValid(String type){
        return (type.endsWith("jpeg") || type.endsWith("bmp") || type.endsWith("png") || type.endsWith("jpg"));
    }

    boolean imageSizeIsValid(long size){
        return  size > 0 && size < 1048576;
    }

    boolean descriptionIsValid(String description){
        return description.length() > 0 && description.length() < 500;
    }

    boolean difficultyIsValidInt(int difficulty){
        return difficulty > 0 && difficulty < 6;
    }

    boolean imageCountIsValid(int size){
        return size < 5;
    }

    boolean partyNameFormatIsValid(String name){
        Pattern r = Pattern.compile("[A-Z,a-z,\\-,_,.,0-9]*");
        Matcher m = r.matcher(name);
        return m.matches();
    }

    boolean partyNameLengthIsValid(String name){
        return name.length() > 1 && name.length()<16;
    }

    boolean difficultyArrayIsValid(int[] difficultyArray){
        return difficultyArray.length == 2 && difficultyArray[0] > 0 && difficultyArray[0] < 6 && difficultyArray[1] > 0 && difficultyArray[1] < 6;
    }

    boolean reviewArrayIsValid(int[] reviewArray){
        return reviewArray.length == 2 && reviewArray[0] > 0 && reviewArray[0] < 6 && reviewArray[1] > 0 && reviewArray[1] < 6;
    }

    boolean coordinatesArrayIsValid(float[] coordinatesArray){
        return coordinatesArray.length == 2;
    }
}
