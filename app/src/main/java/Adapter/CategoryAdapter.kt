package Adapter

import Adapter.CategoryAdapter.ViewHolder
import Domain.CategoryModel
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contrateja.databinding.ViewholderCategoryBinding
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.example.contrateja.Activity.EmployeeFounderActivity


class CategoryAdapter(val items : MutableList<CategoryModel>, private val onItemClick: (CategoryModel) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    private lateinit var context: Context

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(
            android.view.LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.binding.titleTxt.text = item.Name
        Glide.with(context).load(item.Picture).into(holder.binding.imgView)
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, EmployeeFounderActivity::class.java)
            intent.putExtra("profissao", item.Name) // envia o nome da profissão clicada
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}


