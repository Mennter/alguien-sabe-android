package ar.com.soflex.alguiensabe.servicios

import android.content.Context
import android.widget.FrameLayout
import ar.com.soflex.alguiensabe.dominio.Sesion
import ar.com.soflex.alguiensabe.repositorios.SesionRepository
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import io.reactivex.Observable


class SesionService(context: Context) {

    private val spName = SesionService::class.java.name;
    private val prefs = context.getSharedPreferences(spName, Context.MODE_PRIVATE)

    fun get(token: String) : Observable<Sesion> {
        return SesionRepository.create().get(token).map {
            sesion = it
            setInPreference(it)
            return@map it
        }
    }

    fun isActive(): Boolean {
        val sesion: Sesion = getInPreference()
        if (sesion.token != null && !JWT(sesion.token).isExpired(1)) {
            SesionService.sesion = sesion
            return true
        }
        return false
    }

    fun removeSesion() {
        val prefsEditor = prefs.edit()
        prefsEditor.remove("sesion")
        prefsEditor.commit();
    }

    private fun setInPreference(sesion: Sesion) {
        val prefsEditor = prefs.edit()
        prefsEditor.putString("sesion", Gson().toJson(sesion))
        prefsEditor.commit();
    }

    private fun getInPreference(): Sesion {
        return Gson().fromJson(prefs.getString("sesion", "{}"), Sesion::class.java)
    }

    companion object {
        lateinit var sesion: Sesion
    }
}