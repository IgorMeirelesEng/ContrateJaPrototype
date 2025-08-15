package ViewModels
//signin
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

    fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
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

    fun OnNavigateToHomeComplete(){
        _navigateToHome.value = false
    }
}