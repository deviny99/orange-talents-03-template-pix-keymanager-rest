package br.com.zup.chave

import br.com.zup.KeyManagerRegistryGrpc
import br.com.zup.chave.dto.ChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/keys")
class RegistroChaveController(@field:Inject private val registryGrpc: KeyManagerRegistryGrpc
.KeyManagerRegistryBlockingStub) {

    @Post
    fun cadastrar(@Body @Valid chavePixRequest: ChavePixRequest):HttpResponse<String>{

        val responseGrpc = this.registryGrpc.cadastrarChave(chavePixRequest.convertGrpcRequest())

        val uri = UriBuilder.of("/keys/{pixId}")
            .expand(mutableMapOf(Pair("pixId",responseGrpc.id)))

        val response:MutableHttpResponse<String> = HttpResponse.created(uri)
        response.body(mutableMapOf(Pair("pixId",responseGrpc.id)))
        return response
    }

}