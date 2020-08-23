package ar.com.soflex.alguiensabe.servicios

import android.util.Log
import ar.com.soflex.alguiensabe.PreguntaActivity
import ar.com.soflex.alguiensabe.dominio.DetalleRespuesta
import ar.com.soflex.alguiensabe.dominio.Respuesta
import ar.com.soflex.alguiensabe.repositorios.RespuestaRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_pregunta.*

class RespuestaService(val id: String) {

    private val _respuesta: BehaviorSubject<List<Respuesta>> = BehaviorSubject.createDefault(listOf())

    fun load(): Observable<Int> {
        return RespuestaRepository.create().findAllByPreguntaId(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).map {
                _respuesta.onNext(it)
                return@map it.size
            }
    }

    fun get(): Observable<List<Respuesta>> {
        return _respuesta
    }

    fun save(texto: String, detalle: DetalleRespuesta): Observable<Respuesta> {

        val respuesta = createRespuesta(texto, detalle)
        return RespuestaRepository.create().save(respuesta)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).map {
                val lista = _respuesta.value!!.plus(it)
                _respuesta.onNext(lista)
                return@map it
            }
    }

    private fun createRespuesta(texto: String, detalle: DetalleRespuesta): Respuesta {
        return Respuesta(SesionService.sesion.usuario, texto, detalle, id)
    }

}