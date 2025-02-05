package edu.josepinilla.notespmdm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * data class Note
 * crea el objeto Nota con sus atributos
 *
 * @param idNote id de la nota
 * @param title titulo de la nota
 * @param description descripcion de la nota
 * @param date fecha de la nota
 *
 * @author Jose Pinilla
 */
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var idNote: Long = 0,
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
)
