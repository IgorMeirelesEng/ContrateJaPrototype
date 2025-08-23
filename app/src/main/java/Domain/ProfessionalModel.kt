package Domain

class ProfessionalModel(
    var uid: String = "",  // UID do Firebase
    val nome: String = "",
    val profissao: String = "",
    val experience: String = "",
    val fotoUrl: String = ""
) {
    val fotoUrlCorrigida: String
        get() = fotoUrl.replace("\\/", "/") // corrige \/ para /
}
