package edu.pract5.apirestfree.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Animals(
    @SerializedName("characteristics")
    val characteristics: Characteristics?,
    @SerializedName("locations")
    val locations: List<String?>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("taxonomy")
    val taxonomy: Taxonomy?,
    var favorita: Boolean = false
)