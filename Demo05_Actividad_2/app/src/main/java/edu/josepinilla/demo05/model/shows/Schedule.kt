package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Schedule
 * se encarga de almacenar la información del horario
 * de emisión de un show
 *
 * @author Jose Pinilla
 */
data class Schedule(
    @SerializedName("days")
    val days: List<String?>?,
    @SerializedName("time")
    val time: String?
)