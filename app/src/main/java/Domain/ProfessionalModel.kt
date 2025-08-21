package Domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfessionalModel(
    val nome: String = "",
    val profissao: String = "",
    val experience: String = "",
    val fotoUrl: String = ""
) {
    val fotoUrlCorrigida: String
        get() = fotoUrl.replace("\\/", "/") // corrige \/ para /
}


