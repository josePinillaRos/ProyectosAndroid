package edu.pract5.apirestfree.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * method checkConnection
 * Comprueba si hay conexion a internet.
 * @author Jose Pinilla
 *
 * @param context Contexto de la aplicacion.
 */
fun checkConnection(context: Context): Boolean {
    val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetwork
    if (networkInfo != null) {
        val activeNetwork = cm.getNetworkCapabilities(networkInfo)
        if (activeNetwork != null)
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
    }
    return false
}

/**
 * enum class ListaAMostrar
 * Enumeracion de las listas a mostrar, todas o favoritas.
 * @author Jose Pinilla
 */
enum class ListaAMostrar {
    TODAS,
    FAVORITAS
}
var listaAMostrar = ListaAMostrar.TODAS

/**
 * enum class OrdenAlfabetico
 * Enumeracion del orden alfabetico, ascendente o descendente.
 * @author Jose Pinilla
 */
enum class  OrdenAlfabetico {
    ASCENDENTE,
    DESCENDENTE
}

var ordenAlfabetico = OrdenAlfabetico.ASCENDENTE