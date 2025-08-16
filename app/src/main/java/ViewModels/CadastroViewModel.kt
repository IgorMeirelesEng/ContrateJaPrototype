package ViewModels
//Login
import android.widget.EditText
import android.widget.Switch
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contrateja.R
import com.example.contrateja.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CadastroViewModel : ViewModel() {
    private val _CadastroStatus = MutableLiveData<String>()
    val cadastroStatus: LiveData<String> get() = _CadastroStatus

    //_CadastroStatus == _Loginstatus
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome
val database = FirebaseDatabase.getInstance().getReference("usuarios")
    val database1 = FirebaseDatabase.getInstance().getReference("funcionarios")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun cadastro(nome: String,email: String,password: String,cpf: String,profissao: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _CadastroStatus.value = "Cadastro Realizado!"
                    _navigateToHome.value = true
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val dadosFunc = mapOf(
                        "nome" to nome,
                        "email" to email,
                        "password" to password,
                        "cpf" to cpf,
                        "profissao" to profissao
                    )
                    database1.child(uid).setValue(dadosFunc)
                } else {
                    _CadastroStatus.value = "Falha no cadastro: ${task.exception?.message}"
                }
            }
    }
        fun cadastroUsuario(nome: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _CadastroStatus.value = "Cadastro Realizado!"
                    _navigateToHome.value = true
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val dadosUsuario = mapOf (
                        "nome" to nome,
                        "email" to email,
                        "password" to password

                    )
                    database.child(uid).setValue(dadosUsuario)
                } else {
                    _CadastroStatus.value = "Falha no cadastro: ${task.exception?.message}"
                }
            }



    }
    fun OnNavigateToHomeComplete(){
        _navigateToHome.value = false
    }



}