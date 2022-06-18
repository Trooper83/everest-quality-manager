package com.everlution

import grails.gorm.services.Service

@Service(Person)
interface PersonService {

    Person findByEmail(String email)

    List<Person> list(Map args)
}