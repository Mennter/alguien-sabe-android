package ar.com.soflex.alguiensabe.dominio

class Respuesta (
    override val autor: Usuario,
    override val texto: String,
    override val detalle: DetalleRespuesta,
    val padre: String
) : Comentario<DetalleRespuesta> {

    override var id: String? = null
    override var borrado: Boolean = false
}