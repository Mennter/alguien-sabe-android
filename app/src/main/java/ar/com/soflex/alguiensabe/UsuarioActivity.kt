package ar.com.soflex.alguiensabe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import ar.com.soflex.alguiensabe.dominio.ErrorServidor
import ar.com.soflex.alguiensabe.dominio.Usuario
import ar.com.soflex.alguiensabe.repositorios.UsuarioRepository
import ar.com.soflex.alguiensabe.servicios.ErrorService
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_usuario.*
import retrofit2.HttpException


class UsuarioActivity : AppCompatActivity() {

    private var isNameUserValido = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        verificarDisponibilidadUsuario()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun isUsuarioNombreValido(usuario: String): Observable<Boolean> {
        return UsuarioRepository.create()
            .isExist(usuario)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).map {
                return@map !it
            }
    }

    fun onClickGuardar(view: View) {
        if (!isFormularioValid()) return;

        progressBar.visibility = View.VISIBLE
        buttonGuardar.isEnabled = false
        val usuario = getUsuario()
        UsuarioRepository.create()
            .set(usuario)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { usuario: Usuario ->
                    progressBar.visibility = View.GONE
                    buttonGuardar.isEnabled = true
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                },
                {
                    progressBar.visibility = View.GONE
                    buttonGuardar.isEnabled = true
//                    if (error is HttpException) {
//                        val json = error.response().errorBody()?.string()
//                        json?.let {
//                            var errorServidor = Gson().fromJson(json, ErrorServidor::class.java)
//                            Toast.makeText(this, errorServidor.mensaje, Toast.LENGTH_LONG).show()
//                        }
//
//                    } else {
//                        Toast.makeText(this, "Error en el servidor", Toast.LENGTH_LONG).show()
//                    }
                    val error = ErrorService(this).handle(it)
                    Toast.makeText(this, error.mensaje, Toast.LENGTH_LONG).show()
                }
            )
    }


    private fun verificarDisponibilidadUsuario() {
        editTextUsuario.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    isNameUserValido = false
                }
                isUsuarioNombreValido(s.toString()).subscribe(
                    {
                        if (it) {
                            editTextUsuario.error = null
                        } else {
                            editTextUsuario.error = getString(R.string.usuario_usuario_validacion_server)
                        }
                        isNameUserValido = it
                    },
                    { error ->
                        editTextUsuario.error = getString(R.string.usuario_usuario_validacion_server_error)
                        isNameUserValido = false
                    }
                )
            }

        })
    }

    private fun isFormularioValid(): Boolean {

        var valid = true

        if (editTextUsuario.text.toString().isEmpty()) {
            editTextUsuario.error = getString(R.string.usuario_usuario_validacion)
            valid = false
        } else {
            if (isNameUserValido) {
                editTextUsuario.error = null
            } else {
                valid = isNameUserValido
            }
        }

        if (editTextMail.text.toString().isEmpty()) {
            editTextMail.error = getString(R.string.usuario_mail_validacion)
            valid = false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(editTextMail.text.toString()).matches()) {
                editTextMail.error = getString(R.string.usuario_mail_validacion_regexp)
                valid = false
            } else {
                editTextMail.error = null
            }
        }

        if (editTextTexto.text.toString().isEmpty()) {
            editTextTexto.error = getString(R.string.usuario_password_validacion)
            valid = false
        } else {
            editTextTexto.error = null
        }

        if (editTextRePassword.text.toString().isEmpty()) {
            editTextRePassword.error = getString(R.string.usuario_password_reescribir_validacion)
            valid = false
        } else {
            editTextRePassword.error = null
            if (editTextTexto.text.toString() != editTextRePassword.text.toString()) {
                editTextTexto.error = getString(R.string.usuario_password_reescribir_validacion_iguladad)
                editTextRePassword.error = getString(R.string.usuario_password_reescribir_validacion_iguladad)
                valid = false
            } else {
                editTextRePassword.error = null
                editTextRePassword.error = null
            }
        }
        return valid
    }

    private fun getUsuario(): Usuario {
        return Usuario(
            editTextUsuario.text.toString(),
            editTextMail.text.toString(),
            editTextTexto.text.toString()
        )
    }
}