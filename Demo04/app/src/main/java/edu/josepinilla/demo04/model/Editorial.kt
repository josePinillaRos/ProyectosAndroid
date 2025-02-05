package edu.josepinilla.demo04.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Editorial(
    @PrimaryKey(autoGenerate = true) val idEd: Int = 0,
    val name: String? = null
)
