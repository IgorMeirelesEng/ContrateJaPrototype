package com.example.contrateja.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.contrateja.R
import com.example.contrateja.databinding.ActivityFuncionarioMainBinding
import com.google.firebase.database.FirebaseDatabase

class FuncionarioMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFuncionarioMainBinding // <--- binding do layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuncionarioMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val uid = intent.getStringExtra("employee_uid")
        if (!uid.isNullOrEmpty()) {
            val dbRef = FirebaseDatabase.getInstance().getReference("funcionarios").child(uid)
            dbRef.get().addOnSuccessListener { snapshot ->
                val funcionario = snapshot.getValue(ProfessionalMainPageModel::class.java)
                if (funcionario != null) {
                    binding.txtNome.text = funcionario.nome
                    binding.txtProf.text = funcionario.profissao
                    binding.txtExperience.text = "${funcionario.experience} anos de experiência"
                    binding.txtAddress.text = funcionario.adress
                    binding.txtBiography.text = funcionario.biography
                    Glide.with(this)
                        .load(funcionario.fotoUrlCorrigida)
                        .placeholder(R.drawable.outline_account_circle_24)
                        .into(binding.imageView13) // <- Glide agora usa o binding correto
                }
            }
        }
    }
}

