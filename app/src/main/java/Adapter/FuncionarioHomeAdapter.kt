package Adapter

import Domain.ProfessionalModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contrateja.databinding.ViewholderDestaquesBinding

class FuncionarioHomeAdapter(
    private val funcionarios: List<ProfessionalModel>
) : RecyclerView.Adapter<FuncionarioHomeAdapter.FuncionarioViewHolder>() {

    inner class FuncionarioViewHolder(val binding: ViewholderDestaquesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(funcionario: ProfessionalModel) {
            binding.nomePro.text = funcionario.nome
            binding.textpro.text = funcionario.profissao
            binding.textView12.text = "${funcionario.experience} anos de experiência"

            Glide.with(binding.imageView8.context)
                .load(funcionario.fotoUrlCorrigida)
                .placeholder(com.example.contrateja.R.drawable.outline_account_circle_24)
                .into(binding.imageView8)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionarioViewHolder {
        val binding = ViewholderDestaquesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FuncionarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuncionarioViewHolder, position: Int) {
        holder.bind(funcionarios[position])
    }

    override fun getItemCount() = funcionarios.size
}
