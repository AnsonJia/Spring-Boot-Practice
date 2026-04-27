package com.SpringPractice.store;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // handle web requests
public class HomeController {
    //used to get values from the application properties/yaml file
    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/")
    public String index(){
        System.out.println("appName: " + appName);
        return "index.html";
    }
}
