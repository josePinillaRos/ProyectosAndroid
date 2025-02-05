package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Image
 * se encarga de almacenar la información de una imagen
 *
 * @author Jose Pinilla
 */
data class Image(
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("original")
    val original: String?
)