package com.example.contrateja.Activity

import Home.HomeActivity
import ViewModels.CadastroViewModel
import ViewModels.EnterViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
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

        // Lista de profissões
        val opcoes = listOf("Pedreiro","Pintor","Eletricista","Diarista","Fisioterapeuta","Massagista","Esteticista","Cozinheiro(a)")

        // AutoCompleteTextView

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcoes)
        binding.autoProfissao.setAdapter(adapter)
        binding.autoProfissao.visibility = View.GONE

        binding.edtcpf.visibility = View.GONE

        // Switch para mostrar profissão e CPF
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            binding.autoProfissao.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.edtcpf.visibility = if (isChecked) View.VISIBLE else View.GONE

        }
        binding.autoProfissao.setOnClickListener {
            binding.autoProfissao.showDropDown()
        }
        // Botão de cadastro
        binding.buttonEnter.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmailCadastro.text.toString()
            val senha = binding.etSenhaCadastro.text.toString()

            if (binding.switch1.isChecked) {

                val profissaoSelecionada = binding.autoProfissao.text.toString()
                val cpf = binding.edtcpf.text.toString()
                if (name.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() || profissaoSelecionada.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }else if (!opcoes.contains(profissaoSelecionada)){
                    Toast.makeText(this,"Selecione uma profissão válida", Toast.LENGTH_SHORT).show()
                }
                else {
                    cadastroViewModel.cadastro(name, email, senha, cpf, profissaoSelecionada)
                }
            } else {
                if (name.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else {
                    cadastroViewModel.cadastroUsuario(name, email, senha)
                }
            }

            cadastroViewModel.cadastroStatus.observe(this) { status ->
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }

            cadastroViewModel.navigateToHome.observe(this) { navigate ->
                if (navigate) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    cadastroViewModel.OnNavigateToHomeComplete()
                }
            }
        }
    }


    fun navigateToEnter(view: View){
            startActivity(Intent(this, EnterActivity::class.java))
        }

    }

