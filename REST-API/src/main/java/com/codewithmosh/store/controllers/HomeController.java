package com.codewithmosh.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//marking class as a bean (object managed by spring). spring will create an instance of this class and manage it
public class HomeController {
    //example of server side rendering where backend generates the entire HTML page (traditional method rarely used anymore)
    //dynamic HTML using Thymeleaf template engine (most popular template engine)
    @RequestMapping("/")//mapping this method to the root url
    public String index(Model  model) { //give param of type model (container for data to pass data from controller to view)
        model.addAttribute("name", "Anson"); //add attribute with the variable name and value
        return "index"; //remove.html so IntelliJ knows we are working with Thymeleaf template
    }
    //static HTML page
    @RequestMapping("/hello")//mapping to the hello url endpoint
    public String sayHello(){
        return "redirect:/hello.html";// needs a redirect with templates or put /hello.html in the url bar
    }
}
