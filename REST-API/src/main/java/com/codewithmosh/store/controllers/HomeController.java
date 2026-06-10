package com.codewithmosh.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//marking class as a bean (object managed by spring). spring will create an instance of this class and manage it
public class HomeController {
    //example of server side rendering where backend generates the entire HTML page (traditional method rarely used anymore)
    @RequestMapping("/")//mapping this method to the root url
    public String index(){
        return "index.html";
    }
    @RequestMapping("/hello")//mapping to the hello url endpoint
    public String sayHello(){
        return "index.html";
    }//for simplicity, we are returning the same view
}
