package ar.com.soflex.alguiensabe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.soflex.alguiensabe.dominio.*
import ar.com.soflex.alguiensabe.helpers.RespuestaAdapter
import ar.com.soflex.alguiensabe.repositorios.PreguntaRepository
import ar.com.soflex.alguiensabe.repositorios.RespuestaRepository
import ar.com.soflex.alguiensabe.servicios.ErrorService
import ar.com.soflex.alguiensabe.servicios.RespuestaService
import ar.com.soflex.alguiensabe.servicios.SesionService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pregunta.*
import java.util.*
import kotlin.concurrent.schedule

class PreguntaActivity : AppCompatActivity() {

    val viewManager = LinearLayoutManager(this)

    lateinit var respuestaService: RespuestaService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregunta)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.extras?.let {
            it.getString(ID_COMENTARIO)?.let { id ->
                respuestaService = RespuestaService(id)
                getPregunta(id)
                subscribirseRespuestas()
                getRespuestas()
            }
        }

        editTextComentario.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enviarBTN.isEnabled = s.isNotEmpty()

            }
        })

        enviarBTN.isEnabled = editTextComentario.text?.isNotEmpty()?: false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


    fun onClickEnviar(view: View) {

        setRespuesta(editTextComentario.text.toString())

    }

    private fun getPregunta(id: String) {
        progressBarPregunta.visibility = View.VISIBLE
        PreguntaRepository.create().get(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                progressBarPregunta.visibility = View.GONE
                setPregunta(it)

            }, {
                val error = ErrorService(this).handle(it)
                Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
            })

    }


    private fun setPregunta(pregunta: Pregunta) {
        tituloPregunta.visibility = View.VISIBLE
        contenidoPregunta.visibility = View.VISIBLE
        usuarioCategoriaPregunta.visibility = View.VISIBLE

        tituloPregunta.text = pregunta.texto
        contenidoPregunta.text = pregunta.detalle.contenido
        usuarioCategoriaPregunta.text = pregunta.autor.usuario + " - " + pregunta.detalle.categoria

        if (!isAutor(pregunta.autor)) {
            editarBtn.visibility = View.GONE
        }
    }

    private fun isAutor(usuario: Usuario): Boolean {
        return SesionService.sesion.usuario.id == usuario.id
    }


    private fun getRespuestas() {
        progressBarRespuesta.visibility = View.VISIBLE
        respuestaService.load().subscribe({
            progressBarRespuesta.visibility = View.GONE
        }, {
            val error = ErrorService(this).handle(it)
            Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
        })
    }

    private fun subscribirseRespuestas() {
        respuestaService.get().subscribe {
            setRespuestas(it)
        }
    }

    private fun setRespuestas(respuestas: List<Respuesta>) {
        val viewAdapter = RespuestaAdapter(respuestas)
        respuestaRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            if(viewAdapter.itemCount != 0) smoothScrollToPosition(viewAdapter.itemCount - 1)
        }
    }

    private fun setRespuesta(texto: String) {
        respuestaService.save(texto, DetalleRespuesta())
            .subscribe({
                editTextComentario.setText("")
            }, {
                val error = ErrorService(this).handle(it)
                Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
            })
    }

    companion object {
        val ID_COMENTARIO = "id"
    }
}