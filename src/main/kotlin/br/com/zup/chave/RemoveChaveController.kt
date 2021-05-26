package br.com.zup.chave

import br.com.zup.KeyManagerDeleteGrpc
import br.com.zup.chave.dto.RemoveChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/keys")
class RemoveChaveController(@field:Inject private val removeChaveGrpc: KeyManagerDeleteGrpc
.KeyManagerDeleteBlockingStub) {

    @Delete
    fun removerChave(@Body @Valid removeChaveRequest:RemoveChaveRequest):HttpResponse<*>{
        val responseGrpc = this.removeChaveGrpc.removerChave(removeChaveRequest.convertRequestGrpc())
        val response: MutableHttpResponse<String> = HttpResponse.ok()
        response.body(mutableMapOf(Pair("message",responseGrpc.message)))
        return response
    }

}