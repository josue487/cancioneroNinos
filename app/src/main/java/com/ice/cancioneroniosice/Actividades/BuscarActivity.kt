package com.ice.cancioneroniosice.Actividades

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ice.cancioneroniosice.ClasesBase.Cancion
import com.ice.cancioneroniosice.R
import com.ice.cancioneroniosice.Service.SQLiteHelper
import com.ice.cancioneroniosice.databinding.ActivityBuscarBinding


class BuscarActivity : AppCompatActivity() {


    val servicioBD : SQLiteHelper = SQLiteHelper(this,"bdCancioneroChicos.db",null,1)

    //Necesario para trabajar con viewbinding
    private lateinit var binding : ActivityBuscarBinding
    lateinit var cancionBuscada : Cancion
    var notasCancionNiños : String = ""
    var posicion : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflar la actividad
        binding = ActivityBuscarBinding.inflate(layoutInflater)
        //seteo del content view
        setContentView(binding.root)
        //agregado de scrollbar para la cancion que va a ser larga
        binding.lblCancion.movementMethod = ScrollingMovementMethod()
        binding.fmMenu.setClosedOnTouchOutside(true)
        val datos : Intent = getIntent()
        val idHimno: Int = datos.getIntExtra("id",0)
        cargarCancion(idHimno)
    }


    fun cargarCancion(idHimno : Int){

        posicion=idHimno
        cancionBuscada = servicioBD.buscarCancion(idHimno)
        if (cancionBuscada.cancion != ""){
            notasCancionNiños=cancionBuscada.notasCancion
            formatearFavorito(cancionBuscada.favorito)
            presentarEnVista(cancionBuscada.cancion,notasCancionNiños)
        }else{
            Toast.makeText(this, "No existe la cancion buscada", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
    }




    //Codigo para boton subir semitono
    fun botonSemitonoMas(@Suppress("UNUSED_PARAMETER") v : View){
        notasCancionNiños = actualizarNotasArriba(notasCancionNiños)
        presentarEnVista(cancionBuscada.cancion, notasCancionNiños)
    }


    //Codigo para boton bajar semitono
    fun botonSemitonoMenos(@Suppress("UNUSED_PARAMETER") v : View){
        notasCancionNiños = actualizarNotasAbajo(notasCancionNiños)
        presentarEnVista(cancionBuscada.cancion, notasCancionNiños)
    }


    fun actualizarNotasArriba(notasOriginales : String) : String{
        var cancionSemitonoArriba = notasOriginales

        cancionSemitonoArriba=cancionSemitonoArriba.replace(" C#",".D")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" D#",".E")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" F#",".G")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" A#",".B")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" G#",".A")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" C",".C#")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" D",".D#")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" E",".F")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" F",".F#")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" G",".G#")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" A",".A#")
        cancionSemitonoArriba=cancionSemitonoArriba.replace(" B",".C")
        cancionSemitonoArriba=cancionSemitonoArriba.replace("."," ")
        return cancionSemitonoArriba
    }


    fun actualizarNotasAbajo(notasOriginales : String) : String{
        var cancionSemitonoAbajo = notasOriginales
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" C#",".C")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" D#",".D")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" F#",".F")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" G#",".G")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" A#",".A")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" C",".B")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" D",".C#")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" E" , ".D#")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" F" , ".E")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" G",".F#")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" A",".G#")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace(" B",".A#")
        cancionSemitonoAbajo=cancionSemitonoAbajo.replace("."," ")
        return cancionSemitonoAbajo
    }




    //Comentario Formato de la Base de Datos para trabajar
    // Simbolos
    // / significa salto de linea
    // * significa que en este lugar va el coro si o si
    // + Significa que en este lugar va el coro opcionalmente como para que aparezca en multiples ocaciones







    fun presentarEnVista(cancionChicos : String, notasChicos : String){
        var cancionFinal = formatearCancionYNotas(cancionChicos , notasChicos)
        cancionFinal = cancionFinal.replace("/", System.getProperty("line.separator")+ "   ")
        cancionFinal = cancionFinal.replace("+", " ")
        binding.lblCancion.text=cancionFinal
        binding.lblTitulo.text = cancionBuscada.titulo
    }




    fun formatearCancionYNotas(cancion : String, notas : String) : String{

        val aux = notas.replace(" ", "+")
        val cancionSeparada: List<String> = cancion.split("-")
        val notasSeparadas: List<String> = aux.split("-")
        var cancionFinal : String = ""
        for(i in 0..cancionSeparada.size-1)
            cancionFinal = cancionFinal + notasSeparadas[i] + cancionSeparada[i]
        return cancionFinal
    }



    //Codigo para boton atras
    fun atras(@Suppress("UNUSED_PARAMETER") v : View){
        onBackPressed()
    }

    fun formatearFavorito(cancionFavorita : Boolean){
        if(cancionFavorita) binding.menuOpcionFavorito.setImageResource(R.drawable.ic_favorito_on) else binding.menuOpcionFavorito.setImageResource(R.drawable.ic_favorito)
    }


    //Boton de favorito
    fun btnFavorito(v: View){
        formatearFavorito(servicioBD.cambiarEstadoFavorito(cancionBuscada.id))
    }

    fun btnPlayPause(v: View){
        if (true) {
            Toast.makeText(
                this@BuscarActivity,
                "Aun No Esta Disponible Esta Función",
                Toast.LENGTH_SHORT
            ).show()

            val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.al_son_del_cocodrilo)
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause()
                binding.MenuOpcionPlay.setImageResource(R.drawable.ic_play)
            } else {
                mediaPlayer.start()
                binding.MenuOpcionPlay.setImageResource(R.drawable.ic_pause)
            }
        }
        else{
            val url = "https://drive.google.com/open?id=1cEhQvS5Nmnsynvlt44EYhi3cdWC-tSJX" // your URL here
            val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
                try{
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(url)
                    prepareAsync() // might take long! (for buffering, etc)
                    start()
                }catch (error : Error){
                    val mensaje = error.toString()
                    Toast.makeText(this@BuscarActivity, mensaje, Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

}