package ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth



class EnterViewModel :ViewModel(){
    private val _enterStatus = MutableLiveData<String>()
    val enterStatus: LiveData<String> get() = _enterStatus
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, senha: String){
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    _enterStatus.value = "Login bem-sucedido!"
                    _navigateToHome.value = true
                } else{
                    _enterStatus.value = "Falha no login: ${task.exception?.message}"
                    _navigateToHome.value = false
                }
            }

    }
    fun cadastro(nome: String, email: String, senha: String){
        auth.createUserWithEmailAndPassword(email,senha)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    _enterStatus.value = "Cadastro Realizado!"
                    _navigateToHome.value = true
                } else{
                    _enterStatus.value = "Falha no cadastro: ${task.exception?.message}"
                }
            }

    }
    fun OnNavigateToHomeComplete(){
        _navigateToHome.value = false
    }
}