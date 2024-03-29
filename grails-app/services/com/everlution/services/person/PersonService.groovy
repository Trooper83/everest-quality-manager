package com.everlution.services.person

import com.everlution.domains.Person
import grails.gorm.services.Service

@Service(Person)
interface PersonService {

    Person findByEmail(String email)

    List<Person> list(Map args)
}