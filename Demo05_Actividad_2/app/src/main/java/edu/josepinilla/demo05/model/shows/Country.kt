package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Country
 * se encarga de almacenar la información de un país
 *
 * @author Jose Pinilla
 */
data class Country(
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("timezone")
    val timezone: String?
)