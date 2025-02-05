package edu.pract5.apirestfree.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class AnimalsEntity.kt
 * Clase que contiene los datos de los animales para la base de datos.
 * @author Jose Pinilla
 *
 * @param id Identificador del animal.
 * @param name Nombre del animal.
 * @param locationsStr Lugares donde se encuentra el animal.
 * @param favorita Indica si el animal es favorito.
 * @param characteristics Caracteristicas del animal.
 * @param taxonomy Taxonomia del animal.
 */
@Entity(tableName = "animals")
data class AnimalsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String?,

    // Aquí se guarda la lista de locations como String
    val locationsStr: String?,

    var favorita: Boolean = false,

    @Embedded
    val characteristics: Characteristics?,

    @Embedded
    val taxonomy: Taxonomy?
)