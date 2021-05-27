package br.com.zup.controller

import br.com.zup.*
import br.com.zup.chave.dto.PixDetailsRequest
import br.com.zup.chave.dto.PixDetailsResponse
import br.com.zup.chave.extensions.toGrpcTimestamp
import br.com.zup.factory.GrpcClientFactory
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class ConsultaChaveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcKeyManager : KeyManagerSearchGrpc.KeyManagerSearchBlockingStub

    lateinit var pixDetailsRequest: PixDetailsRequest

    @AfterEach
    fun cleanUp(){
        Mockito.reset(this.grpcKeyManager)
    }

    @BeforeEach
    fun setup(){
        pixDetailsRequest = PixDetailsRequest(UUID.randomUUID().toString(),UUID.randomUUID().toString())
    }

    @Test
    fun `Deve consultar pix pela chave`(){

        Mockito.`when`(this.grpcKeyManager.consultarChave(any()))
            .thenReturn(this.responseDetails("",""))

        val request = HttpRequest.GET<PixDetailsResponse>("/keys/details/${UUID.randomUUID()}")
        val response = this.httpClient.toBlocking().exchange(request,Any::class.java)

        with(response){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(HttpStatus.OK.code,status.code)
        }
    }

    @Test
    fun `Deve consultar pix pelo pixId e id do cliente`(){

        Mockito.`when`(this.grpcKeyManager.consultarChave(any()))
            .thenReturn(this.responseDetails(this.pixDetailsRequest.pixId,this.pixDetailsRequest.clienteId))

        val request = HttpRequest.GET<PixDetailsResponse>("/keys/details/${this.pixDetailsRequest.pixId}/" +
                "${this.pixDetailsRequest.clienteId}")
        val response = this.httpClient.toBlocking().exchange(request,Any::class.java)

        with(response){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(HttpStatus.OK.code,status.code)
        }
    }

    @Test
    fun `Nao deve consultar pix por chave nao existente`(){

        Mockito.`when`(this.grpcKeyManager.consultarChave(any()))
            .thenThrow(Status.NOT_FOUND.asRuntimeException())

       val throws =  assertThrows<HttpClientResponseException> {
            val request = HttpRequest.GET<PixDetailsResponse>("/keys/details/${this.pixDetailsRequest.pixId}/" +
                    "${this.pixDetailsRequest.clienteId}")
            this.httpClient.toBlocking().exchange(request,Any::class.java)
           fail("Não era para chegar nesse ponto")
        }


        with(throws){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(HttpStatus.NOT_FOUND.code,status.code)
        }
    }


    private fun responseDetails(pixId:String,clienteId:String):PixDetalhes
    {

        return PixDetalhes.newBuilder()
            .setPixId(pixId)
            .setClienteId(clienteId)
            .setPixValue(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.ALEATORIO)
            .setInstituicao(PixDetalhes.InstituicaoInfo.newBuilder()
                .setIspb("2213123")
                .setAgencia("213123")
                .setNumero("213321")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .build())
            .setTitular(Titular.newBuilder()
                .setNome("fulano")
                .setCpf("111.111.111-11")
                .build())
            .setCreatedAt(LocalDateTime.now().toGrpcTimestamp())
            .build()

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class Clients{
        @Singleton
        fun stubSearch() = Mockito.mock(KeyManagerSearchGrpc
            .KeyManagerSearchBlockingStub::class.java)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
    private fun <T> any(): T = Mockito.any()
}