package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Rating
 * se encarga de almacenar la información de la calificación
 *
 * @author Jose Pinilla
 */
data class Rating(
    @SerializedName("average")
    val average: Double?
)