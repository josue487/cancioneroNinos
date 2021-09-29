package com.ice.cancioneroniosice.Actividades

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ice.cancioneroniosice.ClasesBase.Cancion
import com.ice.cancioneroniosice.Service.SQLiteHelper
import com.ice.cancioneroniosice.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity() {

    //necesario para trabajar con viewbinding
    private lateinit var binding: ActivityMainBinding
    val servicioBD : SQLiteHelper = SQLiteHelper(this,"bdCancioneroChicos.db",null,1)
    var listaCaciones = ArrayList<Cancion>()
    var listaTotal = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflar la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        //seteo del content view
        setContentView(binding.root)
        copiarBaseDeDatos()
        cargarTodos()
        binding.lViewFavoritos.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, BuscarActivity::class.java)
            intent.putExtra("id", listaCaciones.get(position).id)
            startActivity(intent)
        })
    }

    //Carga la totalidad de las canciones en la base de datos para ser seleccionadas y despues expandirlas
    fun cargarTodos(){
        listaCaciones = servicioBD.listarCanciones()
        for (i in listaCaciones){
            listaTotal.add("${i.id} - ${i.titulo}")
        }
        var adaptador = ArrayAdapter(this, R.layout.simple_list_item_1,listaTotal)
        binding.lViewFavoritos.adapter = adaptador
    }


    //Abre actividad favoritos que contiene himnos favoritos
    fun btnFavoritos (@Suppress("UNUSED_PARAMETER") v: View){
        val intent = Intent(this, FavoritosActivity::class.java)
        startActivity(intent)
    }


    //Primera ejecucion copia una base de datos preexistente al sistema
    //Segunda ejecucion no hace nada
    fun copiarBaseDeDatos() {
        val ruta = "/data/data/com.ice.cancioneroniosice/databases/"
        val archivo = "bdCancioneroChicos.db"
        val archivoDB = File(ruta + archivo)
        if (!archivoDB.exists()) {
            try {
                val IS: InputStream = getApplicationContext().getAssets().open(archivo)
                val OS: OutputStream = FileOutputStream(archivoDB)
                val buffer = ByteArray(1024)
                var length : Int
                length = IS.read(buffer)
                while (length > 0) {
                    OS.write(buffer, 0, length)
                    length = IS.read(buffer)
                }
                OS.flush()
                OS.close()
                IS.close()
            } catch (e : FileNotFoundException) {
                Log.d("ERROR", "Archivo no encontrado, " + e.toString())
            } catch (e : IOException) {
                Log.d("ERROR", "Error al copiar la Base de Datos, " + e.toString())
            }
        }
    }
}