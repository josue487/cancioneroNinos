package com.ice.cancioneroniosice.service

import androidx.room.Dao
import androidx.room.Query
import com.ice.cancioneroniosice.clases.Cancion

@Dao
interface CancionDao {
    @Query("SELECT * FROM Cancion")
    fun getAll() :  List<Cancion>

    @Query("SELECT * FROM Cancion WHERE favorito = 1")
    fun listarFavoritos() : List<Cancion>

    @Query("SELECT * FROM Cancion WHERE id = :id")
    fun buscarHimno(id : Int) : Cancion

    @Query("UPDATE Cancion SET favorito = :numeroFavorito WHERE :id LIKE id")
    fun cambiarEstadoFavorito(id : Int, numeroFavorito : Int )
}