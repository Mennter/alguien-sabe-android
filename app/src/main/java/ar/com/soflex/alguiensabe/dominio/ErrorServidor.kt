package ar.com.soflex.alguiensabe.dominio

import com.google.gson.annotations.SerializedName

class ErrorServidor (
    @SerializedName("status")  var status: String,
    @SerializedName("mensaje")  var mensaje: String,
    @SerializedName("error")  var error: String,
    @SerializedName("code")  var code: Long
)