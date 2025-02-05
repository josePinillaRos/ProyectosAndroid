package edu.josepinilla.demo05.model.cast


import com.google.gson.annotations.SerializedName

/**
 * data class Self
 * se encarga de almacenar la información de un link
 *
 * @author Jose Pinilla
 */
data class Self(
    @SerializedName("href")
    val href: String?
)