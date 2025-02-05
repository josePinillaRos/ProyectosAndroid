package edu.josepinilla.filmlist.utils

import android.content.Context
import edu.josepinilla.filmlist.R
import edu.josepinilla.filmlist.model.Film
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Utils {
    companion object {
        fun readRawFile(context: Context): MutableList<Film> {
            val films = mutableListOf<Film>()

            try {
                // Accedemos al archivo RAW directamente usando R.raw.nombre_archivo
                val inputStream = context.resources.openRawResource(R.raw.films)
                val reader = BufferedReader(InputStreamReader(inputStream))

                reader.useLines { lines ->
                    lines.forEach { line ->
                        val parts = line.split(";")

                        if (parts.size == 7) {
                            try {
                                val id = parts[0].trim().toInt()
                                val title = parts[1].trim()
                                val year = parts[2].trim().toInt()
                                val duration = parts[3].trim().toInt()
                                val genre = parts[4].trim()
                                val director = parts[5].trim()
                                val cover = parts[6].trim()

                                val film = Film(id, title, year, duration, genre, director, cover)
                                films.add(film)
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return films
        }
    }

}