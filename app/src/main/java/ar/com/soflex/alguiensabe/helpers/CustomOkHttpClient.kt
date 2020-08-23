package ar.com.soflex.alguiensabe.helpers

import ar.com.soflex.alguiensabe.servicios.SesionService
import okhttp3.OkHttpClient

class CustomOkHttpClient {
    companion object {
        fun build(): OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor {
                    return@addNetworkInterceptor it.proceed(it.request().newBuilder().addHeader("Authorization", "Bearer ${SesionService.sesion.token}").build())
                }
                .build()
        }
    }
}