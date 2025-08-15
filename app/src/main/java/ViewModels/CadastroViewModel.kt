package ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class CadastroViewModel: ViewModel() {
    private val _signInStatus = MutableLiveData<String>()
    val signInStatus: LiveData<String> get() = _signInStatus

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome : LiveData<Boolean> get() = _navigateToHome

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun signIn(email: String, senha: String){
        auth.signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener{ task ->
            if(task.isSuccessful){
                _signInStatus.value = "Login bem-sucedido!"
                _navigateToHome.value = true
            } else{
                _signInStatus.value ="Falha no login: ${task.exception?.message}"
            }
        }
    }
    fun OnNavigateToHomeComplete(){
        _navigateToHome.value = false
    }

}