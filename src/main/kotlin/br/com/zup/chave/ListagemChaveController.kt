package br.com.zup.chave

import br.com.zup.ClienteRequest
import br.com.zup.KeyManagerListKeysGrpc
import br.com.zup.chave.dto.PixInfo
import br.com.zup.chave.extensions.convertChaveType
import br.com.zup.chave.extensions.convertContaType
import br.com.zup.chave.extensions.toLocalDateTime
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/keys")
class ListagemChaveController(@field:Inject private val listagemGrpc: KeyManagerListKeysGrpc
.KeyManagerListKeysBlockingStub) {

    @Get("/{clienteId}")
    fun listarChaves(@PathVariable("clienteId") idCliente:String):HttpResponse<List<PixInfo>>{
        val lista:List<PixInfo> = listagemGrpc.listagemChaves(ClienteRequest.newBuilder().setClienteId(idCliente).build())
            .chavesList.map { PixInfo(it.tipoChave.convertChaveType(),
                it.pixValue,
                it.clienteId,
                it.tipoConta.convertContaType(),
                it.pixID,
                it.createdAt.toLocalDateTime())
            }

        return HttpResponse.ok(lista)
    }


}