package me.hooong.demowebmvc;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class EventValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Event event = (Event)o;
        if (event.getName().equalsIgnoreCase("aaa")) {
            errors.rejectValue("name", "wrongValue","the value is not allowed");
        }
    }
}

//@Component
//public class EventValidator {
//
//    public void validate(Event event, Errors errors) {
//        if (event.getName().equalsIgnoreCase("aaa")) {
//            errors.rejectValue("name", "wrongValue","the value is not allowed");
//        }
//    }
//}
