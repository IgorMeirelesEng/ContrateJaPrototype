package com.example.contrateja.Activity

import ViewModels.EnterViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contrateja.R
import com.example.contrateja.databinding.ActivityEnterBinding

class EnterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEnterBinding
    private val loginViewModel: EnterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)
    binding.cadastreJa.setOnClickListener {
        startActivity(Intent(this, CadastroActivity::class.java))

    }


    }
}


