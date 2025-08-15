package com.example.contrateja.Activity

import ViewModels.CadastroViewModel
import ViewModels.EnterViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contrateja.R
import com.example.contrateja.databinding.ActivityCadastroBinding
import com.example.contrateja.databinding.ActivityEnterBinding
import kotlin.getValue

class CadastroActivity : AppCompatActivity() {
    lateinit var binding : ActivityCadastroBinding
    private val cadastroViewModel: CadastroViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonEnter.setOnClickListener{
            val name = binding.etName.text.toString()
            val email= binding.etEmailCadastro.text.toString()
            val senha = binding.etSenhaCadastro.text.toString()
            if(name.isEmpty() || email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this,"Preecha todos os campos", Toast.LENGTH_SHORT).show()
            } else{
                cadastroViewModel.cadastro(name,email,senha)
            }
            cadastroViewModel.cadastroStatus.observe(this, {
                status ->
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            })
            cadastroViewModel.navigateToHome.observe(this, {
                navigate ->
                if (navigate){
                    startActivity(Intent(this, HomeActivity::class.java))
                    cadastroViewModel.OnNavigateToHomeComplete()
                }
            })

        }
        fun navigateToEnter(view: View){
            startActivity(Intent(this, EnterActivity::class.java))
        }

    }

}