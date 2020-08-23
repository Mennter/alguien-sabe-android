package ar.com.soflex.alguiensabe.helpers

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import ar.com.soflex.alguiensabe.PreguntaActivity
import ar.com.soflex.alguiensabe.R
import ar.com.soflex.alguiensabe.dominio.Pregunta
import ar.com.soflex.alguiensabe.dominio.Respuesta
import ar.com.soflex.alguiensabe.servicios.SesionService

class RespuestaAdapter(private val respuestas: List<Respuesta>) :
    RecyclerView.Adapter<RespuestaAdapter.MyViewHolder>()  {


    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val texto: TextView = v.findViewById(R.id.texto)
        val usuario: TextView = v.findViewById(R.id.usuario)
        val card: LinearLayout = v.findViewById(R.id.card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.respuesta_card, parent, false) as ConstraintLayout
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.texto.text = respuestas[position].texto
        holder.usuario.text = respuestas[position].autor?.usuario?: ""
        val lp = holder.card.layoutParams as ConstraintLayout.LayoutParams

        if (SesionService.sesion.usuario.id == respuestas[position].autor?.id) {
            holder.card.setBackgroundColor(Color.parseColor("#aaaaaa"))
            lp.leftMargin = 36
            lp.topMargin = 16
        } else {
            holder.card.setBackgroundColor(Color.parseColor("#babeee"))
            lp.rightMargin = 36
            lp.topMargin = 16
        }
    }

    override fun getItemCount() = respuestas.size

}