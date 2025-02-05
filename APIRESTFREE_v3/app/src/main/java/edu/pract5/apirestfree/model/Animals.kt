package edu.pract5.apirestfree.model

import com.google.gson.annotations.SerializedName

/**
 * Class Animals.kt
 * Clase que contiene los datos de los animales.
 * @author Jose Pinilla
 *
 * @param characteristics Caracteristicas del animal.
 * @param locations Lugares donde se encuentra el animal.
 * @param name Nombre del animal.
 * @param taxonomy Taxonomia del animal.
 * @param favorita Indica si el animal es favorito.
 */
data class Animals(
    @SerializedName("characteristics")
    val characteristics: Characteristics?,
    @SerializedName("locations")
    val locations: List<String?>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("taxonomy")
    val taxonomy: Taxonomy?,
    var favorita: Boolean = false
)