package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//building api
public class MessageController {
    @RequestMapping("/HelloWorld")//mapping to the url endpoint
    public Message sayHello(){
        return new Message("Hello World!");
    }
}
