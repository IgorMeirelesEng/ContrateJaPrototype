package Home

import Adapter.CategoryAdapter
import Adapter.FuncionarioAdapter
import Adapter.FuncionarioHomeAdapter
import Domain.ProfessionalModel
import ViewModels.HomeViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.contrateja.Activity.EmployeeFounderActivity
import com.example.contrateja.Activity.FuncionarioMainActivity
import com.example.contrateja.R
import com.example.contrateja.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var txtOla: TextView
    private val auth = FirebaseAuth.getInstance()
    private val databaseUsuarios = FirebaseDatabase.getInstance().getReference("usuarios")
    private val databaseFuncionarios = FirebaseDatabase.getInstance().getReference("funcionarios")
    private lateinit var binding: ActivityHomeBinding
    private val viewModel = HomeViewModel()
    private lateinit var adapter: FuncionarioHomeAdapter
    private var listafuncionario = mutableListOf<ProfessionalModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategory()
        txtOla = findViewById(R.id.txtOla)

        adapter = FuncionarioHomeAdapter(listafuncionario) { funcionario ->
            val intent = Intent(this, FuncionarioMainActivity::class.java)
            intent.putExtra("employee_uid", funcionario.uid)
            startActivity(intent)
        }

        binding.recyclerViewDestaque.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewDestaque.adapter = adapter

        carregarFuncionarios()

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

                if (!nomeFunc.isNullOrEmpty()) {
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

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE

        viewModel.category.observe(this, Observer { categorias ->
            binding.recyclerViewServPop.layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewServPop.adapter = CategoryAdapter(categorias) { categoria ->
                val intent = Intent(this, EmployeeFounderActivity::class.java)
                intent.putExtra("profissao", categoria.Name)
                startActivity(intent)
            }
            binding.progressBarCategory.visibility = View.GONE
        })

        viewModel.loadCategory()
    }

    private fun carregarFuncionarios() {
        val dbRef = FirebaseDatabase.getInstance().reference
        binding.progressBar2.visibility = View.VISIBLE

        dbRef.child("funcionarios").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listafuncionario.clear()
                for (child in snapshot.children) {
                    val f = child.getValue(ProfessionalModel::class.java)
                    if (f != null) {
                        f.uid = child.key ?: ""   // 👈 pega a chave do Firebase
                        listafuncionario.add(f)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.progressBar2.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@HomeActivity,
                    "Erro ao carregar: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
