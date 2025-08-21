package com.example.contrateja.Activity

import Home.HomeActivity
import PhotoLoader.Selectionphoto
import ViewModels.CadastroViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.contrateja.databinding.ActivityCadastroBinding

import java.io.File
import java.io.FileOutputStream
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var image: Selectionphoto
    private val IMAGE_PICK_CODE = 1000
    private val cadastroViewModel: CadastroViewModel by viewModels()
    private val FREEIMAGE_API_KEY = "6d207e02198a847aa98d0a2a901485a5" // sua API Key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        image = Selectionphoto(binding.imgPerfil)

        // Lista de profissões
        val opcoes = listOf(
            "Pedreiro", "Pintor", "Eletricista", "Diarista, Mecânico",
            "Fisioterapeuta", "Massagista", "Esteticista", "Cozinheiro(a)"
        )

        // Adapter AutoComplete
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcoes)
        binding.autoProfissao.setAdapter(adapter)

        // Configura visibilidade inicial
        binding.autoProfissao.visibility = View.GONE
        binding.etBiografia.visibility = View.GONE
        binding.etExperiencia.visibility = View.GONE
        binding.edtcpf.visibility = View.GONE
        binding.etEndereco.visibility = View.VISIBLE

        // Observers
        cadastroViewModel.cadastroStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }

        cadastroViewModel.navigateToHome.observe(this) { navigate ->
            if (navigate) {
                startActivity(Intent(this, HomeActivity::class.java))
                cadastroViewModel.OnNavigateToHomeComplete()
            }
        }

        // Switch para mostrar profissão e CPF
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            binding.autoProfissao.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.edtcpf.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.etBiografia.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.etExperiencia.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Seleção de foto
        binding.buttonUploadfoto.setOnClickListener {
            image.pickImage(this, IMAGE_PICK_CODE)
        }

        // Mostrar dropdown
        binding.autoProfissao.setOnClickListener {
            binding.autoProfissao.showDropDown()
        }

        // Botão de cadastro
        binding.buttonEnter.setOnClickListener {
            val imageUri = image.getImageUri()
            if (imageUri == null) {
                Toast.makeText(this, "Selecione uma foto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.etName.text.toString()
            val email = binding.etEmailCadastro.text.toString()
            val senha = binding.etSenhaCadastro.text.toString()
            val telefone = binding.etTelefone.text.toString()
            val adress = binding.etEndereco.text.toString()

            // Exibe a imagem com Glide (trata EXIF e evita travar)
            Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .override(500, 500) // limita tamanho
                .into(binding.imgPerfil)

            // Upload da imagem para FreeImage de forma segura
            uploadImageSafe(imageUri) { link ->
                runOnUiThread {
                    if (link == null) {
                        Toast.makeText(this, "Erro ao enviar a imagem", Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }

                    // Cadastro com link da imagem
                    if (binding.switch1.isChecked) {
                        val biography = binding.etBiografia.text.toString()
                        val experience = binding.etExperiencia.text.toString()
                        val profissaoSelecionada = binding.autoProfissao.text.toString()
                        val cpf = binding.edtcpf.text.toString()

                        if (name.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() ||
                            profissaoSelecionada.isEmpty() || biography.isEmpty() ||
                            telefone.isEmpty() || adress.isEmpty() || experience.isEmpty()
                        ) {
                            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        } else if (!opcoes.contains(profissaoSelecionada)) {
                            Toast.makeText(this, "Selecione uma profissão válida", Toast.LENGTH_SHORT).show()
                        } else {
                            cadastroViewModel.cadastro(
                                name, email, senha, cpf, profissaoSelecionada,
                                telefone, adress, biography, experience, link
                            )
                        }

                    } else {
                        if (name.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty() || adress.isEmpty()) {
                            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        } else {
                            cadastroViewModel.cadastroUsuario(
                                name, email, senha, telefone, adress, link
                            )
                        }
                    }
                }
            }
        }
    }

    // Mostra a foto selecionada imediatamente
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        image.handleActivityResult(requestCode, resultCode, data, IMAGE_PICK_CODE)

        val imageUri = image.getImageUri()
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .override(500, 500)
                .into(binding.imgPerfil)
        }
    }

    fun navigateToEnter(view: View) {
        startActivity(Intent(this, EnterActivity::class.java))
    }

    // Função segura de upload direto do InputStream para FreeImage
    private fun uploadImageSafe(imageUri: Uri, onComplete: (String?) -> Unit) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val tempFile = File(cacheDir, "upload_image.jpg")
            val out = FileOutputStream(tempFile)
            inputStream?.copyTo(out)
            inputStream?.close()
            out.close()

            val client = OkHttpClient()
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", FREEIMAGE_API_KEY)
                .addFormDataPart("source", tempFile.name, tempFile.asRequestBody("image/*".toMediaTypeOrNull()))
                .build()

            val request = Request.Builder()
                .url("https://freeimage.host/api/1/upload")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) = onComplete(null)
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val json = response.body?.string()
                        val link = Regex("\"url\":\"(.*?)\"").find(json!!)?.groups?.get(1)?.value
                        onComplete(link)
                    } else {
                        onComplete(null)
                    }
                }
            })

        } catch (e: Exception) {
            onComplete(null)
        }
    }
}
