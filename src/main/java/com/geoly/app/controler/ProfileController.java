package com.geoly.app.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfileController {

    @GetMapping(path = "/profile/{nickName}")
    public List getProfile(@PathVariable(name = "nickName") String nickName){

        return null;
    }
}
