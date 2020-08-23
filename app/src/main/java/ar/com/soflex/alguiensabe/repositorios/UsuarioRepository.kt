package ar.com.soflex.alguiensabe.repositorios

import ar.com.soflex.alguiensabe.BuildConfig
import ar.com.soflex.alguiensabe.dominio.Usuario
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsuarioRepository {

    @POST("usuario")
    fun set(@Body usuario: Usuario): Observable<Usuario>

    @GET("usuario?projection=_boolean")
    fun isExist(@Query("usuario") usuario: String): Observable<Boolean>



    companion object Factory {
        fun create(): UsuarioRepository {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(UsuarioRepository::class.java)
        }
    }
}