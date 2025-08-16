package Home

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contrateja.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {

    private lateinit var txtOla: TextView
    private val auth = FirebaseAuth.getInstance()
    private val databaseUsuarios = FirebaseDatabase.getInstance().getReference("usuarios")
    private val databaseFuncionarios = FirebaseDatabase.getInstance().getReference("funcionarios")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        txtOla = findViewById(R.id.txtOla)

        val uid = auth.currentUser?.uid
        if (uid != null) {
            // Primeiro verifica se está no nó Funcionarios
            databaseFuncionarios.child(uid).get().addOnSuccessListener { snapshot ->
                val nomeFunc = snapshot.child("nome").value as? String
                if (!nomeFunc.isNullOrEmpty()) {
                    txtOla.text = "Olá, $nomeFunc"
                } else {
                    // Se não encontrar no Funcionarios, verifica no nó Usuarios
                    databaseUsuarios.child(uid).get().addOnSuccessListener { snapUser ->
                        val nomeUser = snapUser.child("nome").value as? String
                        if (!nomeUser.isNullOrEmpty()) {
                            txtOla.text = "Olá, $nomeUser"
                        } else {
                            txtOla.text = "Olá!"
                        }
                    }.addOnFailureListener {
                        txtOla.text = "Olá!"
                    }
                }
            }.addOnFailureListener {
                txtOla.text = "Olá!"
            }
        } else {
            txtOla.text = "Olá!"
        }
    }
}
