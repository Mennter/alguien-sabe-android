package ar.com.soflex.alguiensabe.dominio

import com.google.gson.annotations.SerializedName

interface Comentario<T> {
    val id: String?
    val autor: Usuario
    val texto: String
    val borrado: Boolean
    val detalle: T
}