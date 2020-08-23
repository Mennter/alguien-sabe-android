package ar.com.soflex.alguiensabe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ar.com.soflex.alguiensabe.dominio.Sesion
import ar.com.soflex.alguiensabe.servicios.SesionService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Credentials
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (SesionService(this).isActive()) {
      irPreguntasActivity()
    }


  }


  fun onClickIngreso(view: View) {

    progressBar.visibility = View.VISIBLE
    buttonIngresar.isEnabled = false
    buttonRegistrarse.isEnabled = false
    SesionService(this)
      .get(Credentials.basic(editTextUsuario.text.toString(), editTextTexto.text.toString()))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeOn(Schedulers.io())
      .subscribe(
        { sesion: Sesion ->
          progressBar.visibility = View.GONE
          buttonIngresar.isEnabled = true
          buttonRegistrarse.isEnabled = true
          irPreguntasActivity()
        },
        { error ->
          progressBar.visibility = View.GONE
          buttonIngresar.isEnabled = true
          buttonRegistrarse.isEnabled = true
          if (error is HttpException && error.code() == 401) {
            Toast.makeText(this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_LONG).show()
          } else {
            Toast.makeText(this, "Error en el servidor", Toast.LENGTH_LONG).show()
          }
        }
      )
  }


  fun onClickRegistrarse(view: View) {
    val intent = Intent(this, UsuarioActivity::class.java)
    startActivity(intent)
  }

  private fun irPreguntasActivity() {
    val intent = Intent(this, PreguntasActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
  }
}