package Home

import Adapter.CategoryAdapter
import ViewModels.HomeViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.contrateja.Activity.EmployeeFounderActivity
import com.example.contrateja.R
import com.example.contrateja.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {

    private lateinit var txtOla: TextView
    private val auth = FirebaseAuth.getInstance()
    private val databaseUsuarios = FirebaseDatabase.getInstance().getReference("usuarios")
    private val databaseFuncionarios = FirebaseDatabase.getInstance().getReference("funcionarios")
    private lateinit var binding : ActivityHomeBinding
    private val viewModel = HomeViewModel()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCategory()
        txtOla = findViewById(R.id.txtOla)

        val uid = auth.currentUser?.uid
        if (uid != null) {
            // Primeiro verifica se está no nó Funcionarios
            databaseFuncionarios.child(uid).get().addOnSuccessListener { snapshot ->
                val photoUrlFunc = snapshot.child("fotoUrl").value as? String
                val cleanUrlFunc = photoUrlFunc?.replace("\\", "")
                val nomeFunc = snapshot.child("nome").value as? String
                if (!cleanUrlFunc.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(cleanUrlFunc)
                        .placeholder(R.drawable.outline_account_circle_24)
                        .circleCrop()
                        .into(binding.imagePerfil)
                    Glide.with(this)
                        .load(cleanUrlFunc)
                        .placeholder(R.drawable.outline_account_circle_24)
                        .circleCrop()
                        .into(binding.fotoUnder)
                } else {
                    Glide.with(this)
                        .load(R.drawable.outline_account_circle_24)
                        .circleCrop()
                        .into(binding.imagePerfil)
                    Glide.with(this)
                        .load(R.drawable.outline_account_circle_24)
                        .circleCrop()
                        .into(binding.fotoUnder)

                }
                if (!nomeFunc.isNullOrEmpty() ) {

                    txtOla.text = "Olá, $nomeFunc"

                } else {
                    // Se não encontrar no Funcionarios, verifica no nó Usuarios
                    databaseUsuarios.child(uid).get().addOnSuccessListener { snapUser ->
                        val photoUrlUser = snapUser.child("fotoUrl").value as? String
                        val cleanUrlUser = photoUrlUser?.replace("\\", "")
                        val nomeUser = snapUser.child("nome").value as? String
                        if (!cleanUrlUser.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(cleanUrlUser)
                                .placeholder(R.drawable.outline_account_circle_24)
                                .circleCrop()
                                .into(binding.imagePerfil)
                            Glide.with(this)
                                .load(cleanUrlUser)
                                .placeholder(R.drawable.outline_account_circle_24)
                                .circleCrop()
                                .into(binding.fotoUnder)
                        } else {
                            Glide.with(this)
                                .load(R.drawable.outline_account_circle_24)
                                .circleCrop()
                                .into(binding.imagePerfil)
                            Glide.with(this)
                                .load(R.drawable.outline_account_circle_24)
                                .circleCrop()
                                .into(binding.fotoUnder)
                        }
                        if (!nomeUser.isNullOrEmpty()) {

                            txtOla.text = "Olá, $nomeUser"
                        } else {
                            txtOla.text = "Olá!"
                        }
                    }.addOnFailureListener {
                        Glide.with(this)
                            .load(R.drawable.outline_account_circle_24)
                            .circleCrop()
                            .into(binding.imagePerfil)
                        txtOla.text = "Olá!"
                    }
                }
            }.addOnFailureListener {
                Glide.with(this)
                    .load(R.drawable.outline_account_circle_24)
                    .circleCrop()
                    .into(binding.imagePerfil)
                txtOla.text = "Olá!"
            }
        } else {
            txtOla.text = "Olá!"
        }
    }
    private fun initCategory(){
        binding.progressBarCategory.visibility = View.VISIBLE

        viewModel.category.observe(this, Observer{
            binding.recyclerViewServPop.layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewServPop.adapter = CategoryAdapter(it) { categoria ->
                // Aqui você trata o clique no item
                // Por exemplo, abrir a tela de profissionais daquela categoria
                val intent = Intent(this, EmployeeFounderActivity::class.java)
                intent.putExtra("profissao", categoria.Name)
                startActivity(intent)
            }
            binding.progressBarCategory.visibility =View.GONE
        })
        viewModel.loadCategory()
    }
}
