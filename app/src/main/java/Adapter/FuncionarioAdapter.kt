package Adapter

import Domain.ProfessionalModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contrateja.databinding.ViewholderEmployeelistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FuncionarioAdapter(
    private val items: List<ProfessionalModel>,
    private val onItemClick: (ProfessionalModel) -> Unit
) : RecyclerView.Adapter<FuncionarioAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderEmployeelistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProfessionalModel) {
            binding.nameFunc.text = item.nome ?: "Sem nome"
            binding.nomeProfissaolist.text = item.profissao ?: "Sem profissão"
            binding.experienciaAno.text = item.experience + " anos de experiência"?: "0 anos de experiência"

            Glide.with(binding.imageView9.context)
                .load(item.fotoUrlCorrigida)
                .placeholder(com.example.contrateja.R.drawable.outline_account_circle_24)
                .circleCrop()
                .into(binding.imageView9)

            binding.entrarPaginaFunc.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ViewholderEmployeelistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
