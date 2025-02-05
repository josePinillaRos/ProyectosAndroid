package edu.josepinilla.demo04_v2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SuperHero(
    @PrimaryKey(autoGenerate = true) val idSuper: Int = 0,
    val superName: String? = null,
    val realName: String? = null,
    val favorite: Int = 0,
    //Atributo de la clase para la relacion
    val idEditorial: Int = 0
)