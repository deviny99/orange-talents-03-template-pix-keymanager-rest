package br.com.zup.chave.dto

import br.com.zup.ClienteChaveRequest
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class RemoveChaveRequest(@field:NotBlank val key:String,
                              @field:NotBlank val clienteId:String) {

    fun convertRequestGrpc():ClienteChaveRequest{
        return ClienteChaveRequest.newBuilder()
            .setChave(this.key)
            .setIdClient(this.clienteId)
            .build()
    }
}