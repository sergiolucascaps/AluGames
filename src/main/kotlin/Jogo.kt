data class Jogo(val titulo: String, val capa: String) {
    val descricao = ""

    override fun toString(): String {
        return "Jogo(titulo='$titulo', capa='$capa', descricao='$descricao')"
    }
}