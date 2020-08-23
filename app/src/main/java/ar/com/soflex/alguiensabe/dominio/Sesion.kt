package ar.com.soflex.alguiensabe.dominio

import com.google.gson.annotations.SerializedName

class Sesion (
    @SerializedName("token")  var token: String,
    @SerializedName("usuario")  var usuario: Usuario,
    @SerializedName("ip")  var ip: String
)