package edu.josepinilla.demo05.model.cast


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("self")
    val self: Self?
)