package ar.com.soflex.alguiensabe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.soflex.alguiensabe.dominio.Pregunta
import ar.com.soflex.alguiensabe.dominio.Sesion
import ar.com.soflex.alguiensabe.helpers.PreguntasAdapter
import ar.com.soflex.alguiensabe.servicios.ErrorService
import ar.com.soflex.alguiensabe.servicios.PreguntaService
import ar.com.soflex.alguiensabe.servicios.SesionService
import kotlinx.android.synthetic.main.activity_preguntas.*

class PreguntasActivity : AppCompatActivity() {

    val viewManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preguntas)


        findAll()
        getPreguntas()

        nuevaPreguntaBTN.setOnClickListener {
            val intent = Intent(this, NuevaPreguntaActivity::class.java)
            startActivity(intent)
        }

        editTextTexto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                findAll(s.toString())
            }
        })




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.cerrar_sesion -> cerrarSesion()
            else -> {
            }
        }



        return super.onOptionsItemSelected(item)
    }

    private fun cerrarSesion() {
        SesionService(this).removeSesion()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    private fun findAll(query: String? = null) {
        swipeRefreshLayout.isRefreshing = true
        PreguntaService().load(query)
            .subscribe({
                swipeRefreshLayout.isRefreshing = false
            }, {
                swipeRefreshLayout.isRefreshing = false
                val error = ErrorService(this).handle(it)
                Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
            })
    }

    private fun getPreguntas() {
        PreguntaService().get()
            .subscribe({
                mostrarLista(it)
            }, {
                val error = ErrorService(this).handle(it)
                Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
            })
    }



    private fun mostrarLista(preguntas: List<Pregunta>) {
        val viewAdapter = PreguntasAdapter(preguntas)
        preguntasRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        swipeRefreshLayout.setOnRefreshListener {
            findAll(editTextTexto.text.toString())
        }
    }
}