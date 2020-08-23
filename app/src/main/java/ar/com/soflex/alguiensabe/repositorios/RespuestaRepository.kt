package ar.com.soflex.alguiensabe.repositorios

import ar.com.soflex.alguiensabe.BuildConfig
import ar.com.soflex.alguiensabe.dominio.Comentario
import ar.com.soflex.alguiensabe.dominio.Detalle
import ar.com.soflex.alguiensabe.dominio.DetalleRespuesta
import ar.com.soflex.alguiensabe.dominio.Respuesta
import ar.com.soflex.alguiensabe.helpers.CustomOkHttpClient
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RespuestaRepository {


    @GET("comentario/{id}/comentarios")
    fun findAllByPreguntaId(@Path("id") id: String): Observable<List<Respuesta>>

    @GET("comentario/{id}")
    fun get(@Path("id") id: String): Observable<Respuesta>

    @POST("comentario")
    fun save(@Body comentario: Respuesta): Observable<Respuesta>

    companion object Factory {
        fun create(): RespuestaRepository {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.APP_URL)
                .client(CustomOkHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(RespuestaRepository::class.java)
        }
    }
}