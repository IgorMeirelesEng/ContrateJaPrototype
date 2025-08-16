package com.example.contrateja.Activity

import Home.HomeActivity
import ViewModels.EnterViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
        binding.buttonEnter.setOnClickListener {
            val email= binding.etEmailCadastro.text.toString()
            val senha = binding.etSenhaCadastro.text.toString()
            if(email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this,"Preecha todos os campos", Toast.LENGTH_SHORT).show()
            } else{
                loginViewModel.signIn(email,senha)
            }
            loginViewModel.enterStatus.observe(this, {
                    status ->
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            })
            loginViewModel.navigateToHome.observe(this, {
                    navigate ->
                if (navigate){
                    startActivity(Intent(this, HomeActivity::class.java))
                    loginViewModel.OnNavigateToHomeComplete()
                }
            })
        }
    binding.cadastreJa.setOnClickListener {
        startActivity(Intent(this, CadastroActivity::class.java))

    }


    }
}


