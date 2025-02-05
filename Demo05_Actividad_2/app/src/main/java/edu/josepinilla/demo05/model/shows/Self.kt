package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Self
 * se encarga de almacenar la información de la url de la misma clase
 *
 * @author Jose Pinilla
 */
data class Self(
    @SerializedName("href")
    val href: String?
)