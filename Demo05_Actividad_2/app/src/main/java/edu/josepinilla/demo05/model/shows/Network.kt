package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Network
 * se encarga de almacenar la información de una network
 *
 * @author Jose Pinilla
 */
data class Network(
    @SerializedName("country")
    val country: Country?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("officialSite")
    val officialSite: String?
)