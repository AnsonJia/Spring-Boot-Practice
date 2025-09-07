package com.SpringPractice.store;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy // creates objects only when needed
public class HeavyResource {
    public HeavyResource() {
        System.out.println("Heavy Resource created");
    }
}
