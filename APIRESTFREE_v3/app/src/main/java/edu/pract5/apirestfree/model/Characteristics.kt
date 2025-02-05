package edu.pract5.apirestfree.model

import com.google.gson.annotations.SerializedName

/**
 * Class Characteristics.kt
 * Clase que contiene las caracteristicas de los animales.
 * @author Jose Pinilla
 */
data class Characteristics(
    @SerializedName("age_of_sexual_maturity")
    val ageOfSexualMaturity: String?,
    @SerializedName("age_of_weaning")
    val ageOfWeaning: String?,
    @SerializedName("average_litter_size")
    val averageLitterSize: String?,
    @SerializedName("biggest_threat")
    val biggestThreat: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("common_name")
    val commonName: String?,
    @SerializedName("diet")
    val diet: String?,
    @SerializedName("estimated_population_size")
    val estimatedPopulationSize: String?,
    @SerializedName("gestation_period")
    val gestationPeriod: String?,
    @SerializedName("group")
    val group: String?,
    @SerializedName("group_behavior")
    val groupBehavior: String?,
    @SerializedName("habitat")
    val habitat: String?,
    @SerializedName("height")
    val height: String?,
    @SerializedName("lifespan")
    val lifespan: String?,
    @SerializedName("lifestyle")
    val lifestyle: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("most_distinctive_feature")
    val mostDistinctiveFeature: String?,
    @SerializedName("name_of_young")
    val nameOfYoung: String?,
    @SerializedName("number_of_species")
    val numberOfSpecies: String?,
    @SerializedName("prey")
    val prey: String?,
    @SerializedName("skin_type")
    val skinType: String?,
    @SerializedName("slogan")
    val slogan: String?,
    @SerializedName("top_speed")
    val topSpeed: String?,
    @SerializedName("weight")
    val weight: String?
)