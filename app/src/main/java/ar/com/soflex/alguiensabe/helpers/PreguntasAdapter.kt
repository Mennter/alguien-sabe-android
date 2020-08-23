package ar.com.soflex.alguiensabe.helpers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ar.com.soflex.alguiensabe.PreguntaActivity
import ar.com.soflex.alguiensabe.R
import ar.com.soflex.alguiensabe.dominio.Pregunta

class PreguntasAdapter(private val preguntas: List<Pregunta>) :
    RecyclerView.Adapter<PreguntasAdapter.MyViewHolder>() {

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val texto: TextView
        val usuario: TextView
        val categoria: TextView
        var id: String = ""

        init {
            v.setOnClickListener {
                var myIntent =  Intent(it.context, PreguntaActivity::class.java)
                myIntent.putExtra(PreguntaActivity.ID_COMENTARIO, id)
                it.context.startActivity(myIntent);
            }
            texto = v.findViewById(R.id.texto)
            usuario = v.findViewById(R.id.usuario)
            categoria = v.findViewById(R.id.categoria)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.pregunta_card, parent, false) as ConstraintLayout


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.texto.text = preguntas[position].texto
        holder.usuario.text = preguntas[position].autor?.usuario?: ""
        holder.categoria.text = " " + preguntas[position].detalle?.categoria?: ""
        holder.id = preguntas[position].id?: ""

    }

    override fun getItemCount() = preguntas.size

}
