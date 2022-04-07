package com.ice.cancioneroniosice.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ice.cancioneroniosice.clases.Cancion


@Database(
    entities = [Cancion::class],
    version = 1,
    exportSchema = false
)
abstract class CancionBD : RoomDatabase() {


    abstract fun cancionDao() : CancionDao

    companion object{
        @Volatile
        private var INSTANCE : CancionBD? = null

        fun getDatabase(context:Context):CancionBD{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CancionBD::class.java, "Cancion"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }

}