package edu.josepinilla.demo04.model

import androidx.room.Embedded
import androidx.room.Relation

data class SupersWithEditorial(
    @Embedded val superHero: SuperHero,
    @Relation(
        parentColumn = "idEditorial", //Foreing Key
        entityColumn = "idEd"
    ) val editorial: Editorial
)
