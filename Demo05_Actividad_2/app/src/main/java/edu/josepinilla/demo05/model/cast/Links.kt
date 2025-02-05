package edu.josepinilla.demo05.model.cast


import com.google.gson.annotations.SerializedName


/**
 * data class Links
 * se encarga de almacenar la información de los links
 *
 * @author Jose Pinilla
 */
data class Links(
    @SerializedName("self")
    val self: Self?
)