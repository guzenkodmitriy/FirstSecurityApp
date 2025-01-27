package com.guzenko.springcourse.firstsecurityapp.util;

import com.guzenko.springcourse.firstsecurityapp.models.Person;
import com.guzenko.springcourse.firstsecurityapp.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValdator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValdator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        try {
            personDetailsService.loadUserByUsername(person.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return; //все ок, пользователь не найден
        }

        errors.rejectValue("username", "", "Человек с таким именем уже существует");
    }
}
