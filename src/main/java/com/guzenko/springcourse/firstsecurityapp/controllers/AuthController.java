package com.guzenko.springcourse.firstsecurityapp.controllers;

import com.guzenko.springcourse.firstsecurityapp.dto.AuthenticationDTO;
import com.guzenko.springcourse.firstsecurityapp.dto.PersonDTO;
import com.guzenko.springcourse.firstsecurityapp.models.Person;
import com.guzenko.springcourse.firstsecurityapp.security.JWTUtil;
import com.guzenko.springcourse.firstsecurityapp.services.RegistrationService;
import com.guzenko.springcourse.firstsecurityapp.util.PersonValdator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValdator personValdator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    public AuthController(PersonValdator personValdator, RegistrationService registrationService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValdator = personValdator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
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
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO
                                    , BindingResult bindingResult) {

        Person person = convertToPerson(personDTO);

        //personValdator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors())
            return Map.of("message", "error");

        registrationService.register(person);

        String token = jwtUtil.generateToken(person.getUsername());

        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername()
                    , authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
