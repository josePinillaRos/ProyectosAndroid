package edu.josepinilla.demo05.model.cast

import com.google.gson.annotations.SerializedName

/**
 * data class CastItem
 * se encarga de almacenar la información de un actor
 *
 * @author Jose Pinilla
 */
data class CastItem(
    @SerializedName("character")
    val character: Character?,
    @SerializedName("person")
    val person: Person?,
    @SerializedName("self")
    val self: Boolean?,
    @SerializedName("voice")
    val voice: Boolean?
)