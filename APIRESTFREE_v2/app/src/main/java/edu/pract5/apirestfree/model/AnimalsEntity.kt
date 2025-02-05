package edu.pract5.apirestfree.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class AnimalsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String?,

    // Aquí guardaremos la lista de locations como String
    val locationsStr: String?,

    var favorita: Boolean = false,

    @Embedded
    val characteristics: Characteristics?, // Embed si quieres guardarlas en columnas

    @Embedded
    val taxonomy: Taxonomy?               // Embed si quieres guardarlas en columnas
)