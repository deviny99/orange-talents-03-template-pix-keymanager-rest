package br.com.zup.chave.dto

import br.com.zup.ChaveRequest
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class ChavePixRequest(@field:NotBlank val clienteID:String?,
                           @field:NotBlank @Size(max = 77,message = "") val chave:String?,
                           val tipoChave: ChaveType?,
                           @field:NotBlank val tipoConta: ContaType?) {

    fun convertGrpcRequest(): ChaveRequest {

        var keyType = ChaveType.ALEATORIO
        if (this.tipoChave != null) {
            keyType = this.tipoChave
        }

        return ChaveRequest.newBuilder()
            .setChave(this.chave)
            .setIdClient(this.clienteID)
            .setTipo(keyType.convertProto)
            .setTipoConta(this.tipoConta!!.convertProto)
            .build()
    }

}