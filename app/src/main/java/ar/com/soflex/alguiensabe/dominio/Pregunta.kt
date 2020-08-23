package ar.com.soflex.alguiensabe.dominio

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pregunta (
    @SerializedName("autor") override var autor: Usuario,
    @SerializedName("texto") override var texto: String,
    @SerializedName("detalle") override val detalle: DetallePregunta
) : Comentario<DetallePregunta> {
    @SerializedName("id") override var id: String? = null
    @SerializedName("borrado") override var borrado: Boolean = false

}