package com.paulcruz.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataAuthor(
        @JsonAlias("name") String nameSurname,
        @JsonAlias("birth_year") int birthDate,
        @JsonAlias("death_year") int dateOfDeath
) {
}