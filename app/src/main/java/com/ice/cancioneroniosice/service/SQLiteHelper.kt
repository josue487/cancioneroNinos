package com.ice.cancioneroniosice.service

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getBlobOrNull
import com.ice.cancioneroniosice.clases.Cancion

class SQLiteHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    //Busca una Cancion
    fun findSong (idSong : Int) : Cancion {
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE $idSong LIKE id", null)
        val searchedSong = Cancion(1,"","","",false,"",null)
        if(cursor.moveToFirst()){
            do {
                searchedSong.cancion = cursor.getString(2)
                searchedSong.titulo = cursor.getString(1)
                searchedSong.favorito = cursor.getInt(5)== 1
                searchedSong.urlCancion = cursor.getString(4)
                searchedSong.notasCancion = cursor.getString(3)
                searchedSong.id = cursor.getInt(0)
                searchedSong.oog = cursor.getBlobOrNull(6)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return searchedSong
    }

    //Devuelve la totalidad de las canciones en la base de datos
    fun listSongs () :  ArrayList<Cancion>{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero", null)
        val totalList = ArrayList<Cancion>()
        if(cursor.moveToFirst()){
            do {
                val song = Cancion(1,"","","",false,"",null)
                song.cancion = cursor.getString(2)
                song.titulo = cursor.getString(1)
                song.favorito = cursor.getInt(5)== 1
                song.urlCancion = cursor.getString(4)
                song.notasCancion = cursor.getString(3)
                song.id = cursor.getInt(0)
                song.oog = cursor.getBlobOrNull(6)
                totalList.add(song)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return totalList
    }

    //Devuelve una lista con los favoritos
    fun findFavorites () :  ArrayList<Cancion>{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE favorito LIKE 1", null)
        val listFavorites = ArrayList<Cancion>()
        if(cursor.moveToFirst()){
            do {
                val cancion = Cancion(1,"","","",false,"",null)
                cancion.cancion = cursor.getString(2)
                cancion.titulo = cursor.getString(1)
                cancion.favorito = cursor.getInt(5)== 1
                cancion.urlCancion = cursor.getString(4)
                cancion.notasCancion = cursor.getString(3)
                cancion.id = cursor.getInt(0)
                cancion.oog = cursor.getBlobOrNull(6)
                listFavorites.add(cancion)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return listFavorites
    }


    //Devuelve una lista con los favoritos
    fun findMatches(titulo:String) :  ArrayList<Cancion>{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE titulo like '%$titulo%'", null)
        val listFavorites = ArrayList<Cancion>()
        if(cursor.moveToFirst()){
            do {
                val song = Cancion(1,"","","",false,"",null)
                song.cancion = cursor.getString(2)
                song.titulo = cursor.getString(1)
                song.favorito = cursor.getInt(5)== 1
                song.urlCancion = cursor.getString(4)
                song.notasCancion = cursor.getString(3)
                song.id = cursor.getInt(0)
                song.oog = cursor.getBlobOrNull(6)
                listFavorites.add(song)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return listFavorites
    }


//Cambia el estado de favorito al contrario si es 1 a despues se hace 0 y viseversa
    fun changeStateFavorite(idCancion : Int) : Boolean{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE $idCancion LIKE id", null)
        var favorite = false
        if(cursor.moveToFirst()){
            favorite = cursor.getInt(5) == 1
        }
        cursor.close()
        favorite=favorite.not()
        val numeroFavorito : Int = if (favorite) 1 else 0
        bd.execSQL("UPDATE cancionero SET favorito = $numeroFavorito WHERE $idCancion LIKE id")
        return favorite
    }

}