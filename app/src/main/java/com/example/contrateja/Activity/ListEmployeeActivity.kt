package com.example.contrateja.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contrateja.databinding.ActivityListEmployeeBinding

class ListEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botão voltar funcional
        binding.imageView7.setOnClickListener {
            Toast.makeText(this, "Voltando para Home", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configurar RecyclerView (exemplo)
        binding.listEmployeeView.layoutManager = LinearLayoutManager(this)
        // binding.listEmployeeView.adapter = seuAdapter
    }
}
