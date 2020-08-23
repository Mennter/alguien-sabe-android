package ar.com.soflex.alguiensabe.servicios

import android.util.Log
import ar.com.soflex.alguiensabe.dominio.Pregunta
import ar.com.soflex.alguiensabe.repositorios.PreguntaRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject



class PreguntaService {


    fun load(query: String? = null): Observable<Int> {
        return PreguntaRepository.create().find(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).map {
                _preguntas.onNext(it)
                return@map it.size
            }
    }


    fun get(): Observable<List<Pregunta>> {
        return _preguntas
    }

    fun save(pregunta: Pregunta): Observable<Pregunta> {
        return PreguntaRepository.create()
            .save(pregunta)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).map {
                val lista = listOf(it) + _preguntas.value!!
                _preguntas.onNext(lista)
                return@map it
            }
    }


    companion object {
        private val _preguntas: BehaviorSubject<List<Pregunta>> = BehaviorSubject.createDefault(listOf())
    }
}