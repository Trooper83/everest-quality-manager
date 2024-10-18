package com.manager.quality.everest.services.person

import com.manager.quality.everest.domains.Person
import grails.gorm.services.Service

@Service(Person)
interface PersonService {

    Person findByEmail(String email)

    List<Person> list(Map args)
}