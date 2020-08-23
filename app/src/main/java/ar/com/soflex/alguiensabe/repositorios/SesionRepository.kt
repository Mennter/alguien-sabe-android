package ar.com.soflex.alguiensabe.repositorios

import ar.com.soflex.alguiensabe.BuildConfig
import ar.com.soflex.alguiensabe.dominio.Sesion
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface SesionRepository {

    @GET("sesion")
    fun get(@Header("Authorization") token:String): Observable<Sesion>


    companion object Factory {
        fun create(): SesionRepository {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(SesionRepository::class.java)
        }
    }
}