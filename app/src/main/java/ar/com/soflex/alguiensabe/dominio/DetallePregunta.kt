package ar.com.soflex.alguiensabe.dominio

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetallePregunta(
    val categoria: String,
    val contenido: String
): Detalle