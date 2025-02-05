package edu.josepinilla.demo04_v2.data

import edu.josepinilla.demo04_v2.model.Editorial
import edu.josepinilla.demo04_v2.model.SuperHero
import kotlinx.coroutines.flow.Flow

class SupersRepository(val dataSource: SupersDataSource) {
    val allSuperHeroes: Flow<List<SuperHero>> = dataSource.allSuperHeroes
    val allSupersWithEditorial= dataSource.allSupersWithEditorial
    val allEditorial = dataSource.allEditorial
    val numEditorials: Flow<Int> = dataSource.numEditorials

    //Eliminar super heroe que sigue el flujo de informacion desde la base de datos
    suspend fun deleteSuperHero(superHero: SuperHero) {
        dataSource.deleteSuperHero(superHero)
    }

    suspend fun insertEditorial(editorial: Editorial) {
        dataSource.insertEditorial(editorial)
    }

    suspend fun insertSuperHero(superHero: SuperHero) {
        dataSource.insertSuperHero(superHero)
    }

    suspend fun getEditorialById(editorialId: Int) = dataSource.getEditorialById(editorialId)
    suspend fun getSuperById(idSuper: Int) = dataSource.getSuperById(idSuper)
}