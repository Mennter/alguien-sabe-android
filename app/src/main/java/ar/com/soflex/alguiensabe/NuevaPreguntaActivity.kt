package ar.com.soflex.alguiensabe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import ar.com.soflex.alguiensabe.dominio.DetallePregunta
import ar.com.soflex.alguiensabe.dominio.Pregunta
import ar.com.soflex.alguiensabe.repositorios.PreguntaRepository
import ar.com.soflex.alguiensabe.servicios.ErrorService
import ar.com.soflex.alguiensabe.servicios.PreguntaService
import ar.com.soflex.alguiensabe.servicios.SesionService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_nueva_pregunta.*
import kotlinx.android.synthetic.main.activity_nueva_pregunta.progressBar
import java.util.*
import kotlin.concurrent.schedule

class NuevaPreguntaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_pregunta)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var adapter = ArrayAdapter.createFromResource(
            this,
            R.array.nueva_pregunta_categorias_lista,
            R.layout.list_item
        )

        autoCompleteTextViewtCategoria.setAdapter(adapter)
        autoCompleteTextViewtCategoria.setText(adapter.getItem(0).toString())
        adapter.filter.filter(null);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


    fun onClickGuardar(view: View) {
        save()
    }


    private fun getPregunta(): Pregunta {
        val detallePregunta = DetallePregunta(
            autoCompleteTextViewtCategoria.text.toString(),
            editTextContenido.text.toString()
        )

        return Pregunta(
            SesionService.sesion.usuario,
            editTextTexto.text.toString(),
            detallePregunta
        )
    }

    private fun save() {
        progressBar.visibility = View.VISIBLE
        PreguntaService().save(getPregunta())
            .subscribe({
                progressBar.visibility = View.GONE
                Toast.makeText(this, getText(R.string.nueva_pregunta_exito), Toast.LENGTH_LONG).show()
                finish()
                
            }, { error ->
                progressBar.visibility = View.GONE

                val error = ErrorService(this).handle(error)
                Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
            })
    }
}