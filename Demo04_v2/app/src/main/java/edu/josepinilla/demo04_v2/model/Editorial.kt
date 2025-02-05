package edu.josepinilla.demo04_v2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "nombre que quiero para la tabla")
@Entity
data class Editorial(
    @PrimaryKey(autoGenerate = true)
    val idEd : Int = 0,
    val name : String? = null
)