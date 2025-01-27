package com.guzenko.springcourse.firstsecurityapp.controllers;

import com.guzenko.springcourse.firstsecurityapp.models.Person;
import com.guzenko.springcourse.firstsecurityapp.services.RegistrationService;
import com.guzenko.springcourse.firstsecurityapp.util.PersonValdator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValdator personValdator;
    private final RegistrationService registrationService;

    public AuthController(PersonValdator personValdator, RegistrationService registrationService) {
        this.personValdator = personValdator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person
                                    , BindingResult bindingResult) {

        personValdator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(person);

        return "redirect:/auth/login";
    }
}
