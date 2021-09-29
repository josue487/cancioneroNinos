package com.ice.cancioneroniosice.Service

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ice.cancioneroniosice.ClasesBase.Cancion

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
    fun buscarCancion (idCancion : Int) : Cancion {
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE $idCancion LIKE id", null)
        val cancionBuscada = Cancion()
        if(cursor.moveToFirst()){
            do {
                cancionBuscada.cancion = cursor.getString(2)
                cancionBuscada.titulo = cursor.getString(1)
                cancionBuscada.favorito = cursor.getInt(5)== 1
                cancionBuscada.urlCancion = cursor.getString(4)
                cancionBuscada.notasCancion = cursor.getString(3)
                cancionBuscada.id = cursor.getInt(0)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return cancionBuscada
    }

    //Devuelve la totalidad de las canciones en la base de datos
    fun listarCanciones () :  ArrayList<Cancion>{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero", null)
        var listaTotal = ArrayList<Cancion>()
        if(cursor.moveToFirst()){
            do {
                val cancion = Cancion()
                cancion.cancion = cursor.getString(2)
                cancion.titulo = cursor.getString(1)
                cancion.favorito = cursor.getInt(5)== 1
                cancion.urlCancion = cursor.getString(4)
                cancion.notasCancion = cursor.getString(3)
                cancion.id = cursor.getInt(0)
                listaTotal.add(cancion)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return listaTotal
    }

    //Devuelve una lista con los favoritos
    fun buscarFavoritos () :  ArrayList<Cancion>{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE favorito LIKE 1", null)
        var listaFavoritos = ArrayList<Cancion>()
        if(cursor.moveToFirst()){
            do {
                val cancion = Cancion()
                cancion.cancion = cursor.getString(2)
                cancion.titulo = cursor.getString(1)
                cancion.favorito = cursor.getInt(5)== 1
                cancion.urlCancion = cursor.getString(4)
                cancion.notasCancion = cursor.getString(3)
                cancion.id = cursor.getInt(0)
                listaFavoritos.add(cancion)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return listaFavoritos
    }


//Cambia el estado de favorito al contrario si es 1 a despues se hace 0 y viseversa
    fun cambiarEstadoFavorito(idCancion : Int) : Boolean{
        val bd : SQLiteDatabase = readableDatabase
        val cursor : Cursor = bd.rawQuery("SELECT * FROM cancionero WHERE $idCancion LIKE id", null)
        var favorito : Boolean = false
        if(cursor.moveToFirst()){
            favorito = cursor.getInt(5) == 1
        }
        cursor.close()
        favorito=favorito.not()
        val numeroFavorito : Int = if (favorito) 1 else 0
        bd.execSQL("UPDATE cancionero SET favorito = $numeroFavorito WHERE $idCancion LIKE id")
        return favorito
    }

}