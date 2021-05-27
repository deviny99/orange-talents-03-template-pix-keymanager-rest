package br.com.zup.chave

import br.com.zup.KeyManagerSearchGrpc
import br.com.zup.PixRequest
import br.com.zup.chave.dto.PixDetailsResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@Controller("/keys")
class ConsultaChaveController(private val grpc: KeyManagerSearchGrpc.KeyManagerSearchBlockingStub) {

    @Get("/details/{key}")
    fun consultarChavePorChave(@PathVariable("key") key:String ):HttpResponse<PixDetailsResponse>{

        val retornoGrpc = grpc.consultarChave(PixRequest.newBuilder().setChave(key).build())

        return HttpResponse.ok(PixDetailsResponse(retornoGrpc))
    }


    @Get("/details/{pixId}/{clienteId}")
    fun consultarChavePorChave(@PathVariable("pixId") pixId:String,
                               @PathVariable("clienteId") clienteId:String )
    :HttpResponse<PixDetailsResponse>{

        val retornoGrpc = grpc.consultarChave(PixRequest.newBuilder()
            .setPixId(PixRequest.FiltroPorPixId.newBuilder()
                .setPixId(pixId)
                .setClienteId(clienteId)
                .build())
            .build())

        return HttpResponse.ok(PixDetailsResponse(retornoGrpc))
    }

}