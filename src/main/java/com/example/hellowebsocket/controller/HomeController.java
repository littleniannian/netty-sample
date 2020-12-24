package com.example.hellowebsocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName HomeController.java
 * @Description 页面引导controller
 * @createTime 2020年12月24日 10:46:00
 */
@RestController
public class HomeController {
    @GetMapping("/index")
    public ModelAndView index(){
        ModelAndView mav=new ModelAndView("/socket.html");
        mav.addObject("uid", new Random().nextInt(10));
        return mav;
    }
}
