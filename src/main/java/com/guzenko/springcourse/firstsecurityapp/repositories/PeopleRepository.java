package com.guzenko.springcourse.firstsecurityapp.repositories;

import com.guzenko.springcourse.firstsecurityapp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUsername(String name);
}
