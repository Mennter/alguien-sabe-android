package ar.com.soflex.alguiensabe.repositorios

import ar.com.soflex.alguiensabe.BuildConfig
import ar.com.soflex.alguiensabe.dominio.Comentario
import ar.com.soflex.alguiensabe.dominio.Detalle
import ar.com.soflex.alguiensabe.dominio.Pregunta
import ar.com.soflex.alguiensabe.helpers.CustomOkHttpClient
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface PreguntaRepository {

    @GET("comentario")
    fun find(@Query("q") q: String? = null): Observable<List<Pregunta>>


    @GET("comentario/{id}")
    fun get(@Path("id") id: String): Observable<Pregunta>

    @POST("comentario")
    fun save(@Body comentario: Pregunta): Observable<Pregunta>

    companion object Factory {
        fun create(): PreguntaRepository {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.APP_URL)
                .client(CustomOkHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(PreguntaRepository::class.java)
        }
    }

}