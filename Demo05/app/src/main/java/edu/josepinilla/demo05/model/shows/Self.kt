package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

data class Self(
    @SerializedName("href")
    val href: String?
)