package ar.com.soflex.alguiensabe.dominio

import com.google.gson.annotations.Expose

class Usuario (
    @Expose(serialize = true, deserialize = true) var usuario: String,
    @Expose(serialize = true, deserialize = true) var mail: String,
    @Expose(serialize = true, deserialize = true) var password: String = ""
) {
    @Expose(serialize = true, deserialize = false)  var id: Long = 0
    @Expose(serialize = true, deserialize = false)  var bloqueado: Boolean = false
    @Expose(serialize = true, deserialize = false)  var borrado: Boolean = false
    @Expose(serialize = true, deserialize = false)  var alta: String = ""
}