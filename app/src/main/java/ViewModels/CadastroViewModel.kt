package ViewModels
//Login
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class CadastroViewModel : ViewModel() {
    private val _CadastroStatus = MutableLiveData<String>()
    val cadastroStatus: LiveData<String> get() = _CadastroStatus

    //_CadastroStatus == _Loginstatus
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _CadastroStatus.value = "Login bem-sucedido!"
                    _navigateToHome.value = true
                } else {
                    _CadastroStatus.value = "Falha no login: ${task.exception?.message}"
                }
            }
    }

    fun cadastro(nome: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _CadastroStatus.value = "Cadastro Realizado!"
                    _navigateToHome.value = true
                } else {
                    _CadastroStatus.value = "Falha no cadastro: ${task.exception?.message}"
                }
            }

    }
    fun OnNavigateToHomeComplete(){
        _navigateToHome.value = false
    }



}