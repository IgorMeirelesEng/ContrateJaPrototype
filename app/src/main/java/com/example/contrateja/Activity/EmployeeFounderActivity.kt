package com.example.contrateja.Activity

import Adapter.FuncionarioAdapter
import Domain.ProfessionalModel
import Home.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contrateja.databinding.ActivityListEmployeeBinding
import com.google.firebase.database.FirebaseDatabase

class EmployeeFounderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListEmployeeBinding
    private val databaseFunc = FirebaseDatabase.getInstance().getReference("funcionarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView7.setOnClickListener {
            finish() // fecha a Activity e volta para a HomeActivity
        }

        val profissao = intent.getStringExtra("profissao") ?: ""
        binding.txtProfissao.text = "$profissao"
        binding.listEmployeeView.layoutManager = LinearLayoutManager(this)
        binding.progressBar.visibility = View.VISIBLE

        databaseFunc.get().addOnSuccessListener { snapshot ->
            val lista = mutableListOf<ProfessionalModel>()
            for (child in snapshot.children) {
                val func = child.getValue(ProfessionalModel::class.java)
                if (func != null && func.profissao == profissao) {
                    func.uid = child.key ?: ""   // 👈 pega a chave
                    lista.add(func)
                }
            }


            binding.progressBar.visibility = View.GONE

            if (lista.isEmpty()) {
                Toast.makeText(
                    this,
                    "Nenhum profissional encontrado para $profissao",
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.listEmployeeView.adapter = FuncionarioAdapter(lista) { funcionario ->
                val intent = Intent(this, FuncionarioMainActivity::class.java)
                intent.putExtra("employee_uid", funcionario.uid)
                startActivity(intent)
            }
        }.addOnFailureListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Erro ao carregar profissionais", Toast.LENGTH_SHORT).show()
        }
    }
}
