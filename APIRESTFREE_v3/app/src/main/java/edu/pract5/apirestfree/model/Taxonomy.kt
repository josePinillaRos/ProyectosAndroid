package edu.pract5.apirestfree.model

import com.google.gson.annotations.SerializedName

/**
 * Class Taxonomy.kt
 * Clase que contiene la taxonomia de los animales.
 * @author Jose Pinilla
 */
data class Taxonomy(
    @SerializedName("class")
    val classX: String?,
    @SerializedName("family")
    val family: String?,
    @SerializedName("genus")
    val genus: String?,
    @SerializedName("kingdom")
    val kingdom: String?,
    @SerializedName("order")
    val order: String?,
    @SerializedName("phylum")
    val phylum: String?,
    @SerializedName("scientific_name")
    val scientificName: String?
)