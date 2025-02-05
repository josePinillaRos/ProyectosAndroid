package edu.josepinilla.demo04_v2.data

import edu.josepinilla.demo04_v2.model.Editorial
import edu.josepinilla.demo04_v2.model.SuperHero
import edu.josepinilla.demo04_v2.model.SupersWithEditorial
import kotlinx.coroutines.flow.Flow

class SupersDataSource(val db: SupersDao) {
    val allSuperHeroes: Flow<List<SuperHero>> = db.getAllSuperHeroes()
    val allSupersWithEditorial: Flow<List<SupersWithEditorial>> = db.getSuperHeroesWithEditorial()
    val allEditorial: Flow<List<Editorial>> = db.getAllEditorials()
    val numEditorials: Flow<Int> = db.getNumEditorials()

    //Pasamos la funcion de eliminar super heroe al data source
    suspend fun deleteSuperHero(superHero: SuperHero) {
        db.deleteSuperHero(superHero)
    }

    suspend fun insertEditorial(editorial: Editorial) {
        db.insertEditorial(editorial)
    }

    suspend fun insertSuperHero(superHero: SuperHero) {
        db.insertSuperHero(superHero)
    }

    suspend fun getSuperById(idSuper: Int): SupersWithEditorial? = db.getSuperById(idSuper)

    suspend fun getEditorialById(editorialId: Int): Editorial? = db.getEditorialById(editorialId)
}