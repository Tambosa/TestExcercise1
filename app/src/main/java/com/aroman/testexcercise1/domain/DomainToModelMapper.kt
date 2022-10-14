package com.aroman.testexcercise1.domain

interface DomainToModelMapper<Domain : Any, Model : Any> {
    fun toModel(value: Domain): Model
    fun fromModel(value: Model): Domain
}