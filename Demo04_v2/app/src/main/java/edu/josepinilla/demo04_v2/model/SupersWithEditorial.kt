package edu.josepinilla.demo04_v2.model

import androidx.room.Embedded
import androidx.room.Relation

data class SupersWithEditorial(
    //clase que va a absorber datos de la tabla
    @Embedded val superHero: SuperHero,
    //Relacion que va a tener la clase
    @Relation(
        //columna de la relacion que va a hacer de padre, se tiene que llamar igual que el atributo de la clase
        parentColumn = "idEditorial",
        //foreign key en superhero
        entityColumn = "idEd"
        //primary key en editorial
    ) val editorial: Editorial
)