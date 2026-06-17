package com.codewithmosh.store.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//boilerplate code for defining a custom validation annotation (doesn't contain the logic)
@Target(ElementType.FIELD) //target annotation tells us this where we want to apply this annotation (field in this case, {} for multi)
@Retention(RetentionPolicy.RUNTIME)//this annotation will be available at runtime (so that the validator can access it)
@Constraint(validatedBy = LowercaseValidator.class)//specifies the class that will implement the validation logic for this annotation
public @interface Lowercase {// annotation class represented with @interface (declaration so does not contain any logic)
    //define the message attribute (used to specify the error message when validation fails) and give it a default value
    String message() default "must be lowercase";
    //define the groups attribute (used for grouping constraints)
    Class<?>[] groups() default {};// not used in this example, so we give it a default empty array
    //define the payload attribute (used for carrying metadata information about the validation failure)
    Class<? extends Payload>[] payload() default {}; //not used in this example, so we give it a default empty array
}
