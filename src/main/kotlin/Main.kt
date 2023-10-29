import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.util.Scanner

fun main() {

    val scan = Scanner(System.`in`)
    println("Digite o Id de busca")
    val id = scan.nextLine()
    val url = "https://www.cheapshark.com/api/1.0/games?id=$id"

    val client: HttpClient = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build()

    val response = client
        .send(request, BodyHandlers.ofString())

    if(response.statusCode() != 200)
    {
        println("Falha na busca pelo Id: $id")
        return;
    }

    val json = response.body()
    val gson = Gson()

    var meuJogo: Jogo? = null

    val run = runCatching {
        val meuInfoJogo = gson.fromJson(json, InfoJogo::class.java)
        meuJogo = Jogo(meuInfoJogo.info.title, meuInfoJogo.info.thumb)
    }

    run.onFailure {
        println("Falha na criação do objeto do jogo")
        return
    }

    run.onSuccess {

        println("Deseja adicionar uma descrição personalizada ao Jogo? S/N")
        val op = Scanner(System.`in`)

        if(op.nextLine().equals("S", ignoreCase = true))
        {
            println("Digite a nova descrição:")
            val novaDescricao = Scanner(System.`in`).nextLine()

            if(novaDescricao.isNotEmpty())
                meuJogo?.descricao = novaDescricao
            else
                meuJogo?.descricao = meuJogo?.titulo
        } else
            meuJogo?.descricao = meuJogo?.titulo

        println(meuJogo)
    }
}