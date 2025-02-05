package edu.actividad.demo06.data

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * RepositoryFirebase
 * Clase que representa el repositorio de Firebase
 *
 * @author Jose Pinilla
 */
class RepositoryFirebase {
    private val TAG = RepositoryFirebase::class.java.simpleName
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val namespace: String = "Jose Pinilla"

    /**
     * createDocument
     * Método que crea un documento en Firebase
     *
     * @author Jose Pinilla
     */
    fun createDocument() {
        val docRef = db.collection("demo06").document(namespace)
        // Chequea si el documento existe
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                Log.i(TAG, "createdDocument: Document data: ${document.data}")
            } else {
                Log.i(TAG, "createDocument: No such document")
                // Create a empty document with the namespace as the document ID
                docRef.set(mapOf("cities" to emptyList<Map<String, String>>())) // Empty
                    .addOnSuccessListener {
                        Log.i(TAG, "createDocument: Added with ID: ${namespace}")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "createDocument: Error adding document", e)
                    }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "createDocument: get failed with ", exception)
        }
    }

    /**
     * addCity
     * Método que agrega una ciudad a Firebase
     *
     * @param city nombre de la ciudad
     * @param countryCode código del país
     *
     * @author Jose Pinilla
     */
    fun addCity(city: String, countryCode: String) {
        val newCity: List<Map<String, String>> = listOf(
            mapOf("name" to city, "countryCode" to countryCode)
        )
        val docRef = db.collection("demo06").document(namespace)

        docRef.update("cities", FieldValue.arrayUnion(*newCity.toTypedArray()))
            .addOnSuccessListener {
                Log.i(TAG, "addCity: DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "addCity: Error updating document", e)
            }
    }

    /**
     * fetchArrayCities
     * Método que obtiene un flujo de ciudades de Firebase
     *
     * @return Flow<List<Map<String, String>>> flujo de ciudades
     *
     * @autor Jose Pinilla
     */
    fun fetchArrayCities(): Flow<List<Map<String, String>>> = callbackFlow {
        val listenerRegistration = db.collection("demo06").document(namespace)
            .addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    Log.e(TAG, "fetchArrayCities: get failed with ${e.message}")
                    close(e) // Close the flow with an exception
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.i(TAG, "fetchArrayCities: Data: ${documentSnapshot.data}")
                    // 1. as? List<*>: Realiza un casting seguro a una lista genérica
                    // (List<*>) para evitar errores en tiempo de ejecución.
                    // 2. mapNotNull filtra elementos que no sean mapas, asegurando que
                    // solo procesas datos válidos.
                    // 3. filterKeys y filterValues filtran las claves y los valores
                    // para garantizar que sean del tipo String.
                    // 4. mapKeys y mapValues realizan el casting seguro de las claves
                    // y valores a String.
                    val cities: List<Map<String, String>> =
                        (documentSnapshot.get("cities") as? List<*>)
                            ?.mapNotNull { it as? Map<*, *> } // Convertir cada elemento
                            ?.map { cityMap ->
                                cityMap.filterKeys { it is String }
                                    .filterValues { it is String }
                                    .mapKeys { it.key as String }
                                    .mapValues { it.value as String }
                            } ?: emptyList()
                    trySend(cities) // Send data to the flow
                } else {
                    Log.e(TAG, "fetchArrayCities: No such document")
                    trySend(emptyList()) // Send empty list to the flow
                }
            }
        awaitClose{ listenerRegistration.remove() }
    }

    /**
     * fetchArrayAllCitiesDocs
     * Método que obtiene un flujo de ciudades de Firebase
     * agrupadas por nombre y countryCode
     *
     * @return Flow<List<Map<Map<String, String>, Int>>> flujo de ciudades
     *
     * @autor Jose Pinilla
     */
    fun fetchArrayAllCitiesDocs(): Flow<List<Map<Map<String, String>, Int>>> = callbackFlow {
        val listenerRegistration = db.collection("demo06")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "fetchArrayAllCitiesDocs: Error -> ${error.message}")
                    close(error) // Cierra el flujo si hay error
                    return@addSnapshotListener
                }

                if (value != null && !value.isEmpty) {
                    // 1) Concatenamos (flatMap) todas las ciudades de todos los documentos
                    val allCities = value.documents.flatMap { doc ->
                        val citiesList = doc.get("cities") as? List<*>
                        // Convertimos cada elemento a Map<String, String> con comprobaciones
                        citiesList?.mapNotNull { it as? Map<String, String> } ?: emptyList()
                    }

                    // 2) Agrupamos las ciudades por name y countryCode
                    //    y contamos con eachCount()
                    val groupedCounts: Map<Map<String, String>, Int> = allCities
                        .groupingBy { city ->
                            mapOf(
                                "name" to (city["name"] ?: ""),
                                "countryCode" to (city["countryCode"] ?: "")
                            )
                        }
                        .eachCount()

                    // 3) Transformamos el Map en List<Map<Map<String,String>,Int>>
                    val result = groupedCounts.map { (cityKey, count) ->
                        mapOf(cityKey to count)
                    }

                    trySend(result) // Emitimos el resultado
                } else {
                    trySend(emptyList()) // Si no hay documentos
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}