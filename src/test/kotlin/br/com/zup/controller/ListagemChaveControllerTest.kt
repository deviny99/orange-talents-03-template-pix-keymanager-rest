package br.com.zup.controller

import br.com.zup.*
import br.com.zup.chave.dto.PixInfo
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
class ListagemChaveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcKeyManager : KeyManagerListKeysGrpc.KeyManagerListKeysBlockingStub

    private lateinit var clienteRequest: ClienteRequest

    @AfterEach
    fun cleanUp(){
        Mockito.reset(this.grpcKeyManager)
    }

    @BeforeEach
    fun setup(){

    }

    @Test
    fun `Deve listar chaves do cliente`(){

        Mockito.`when`(this.grpcKeyManager.listagemChaves(any(ClienteRequest::class.java)))
            .thenReturn(ListaPix.newBuilder()
                .addChaves(PixDetalhesLista.newBuilder()
                    .setPixValue(UUID.randomUUID().toString())
                    .setTipoChave(TipoChave.ALEATORIO)
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .setClienteId(UUID.randomUUID().toString())
                    .setPixID(UUID.randomUUID().toString())
                    .setCreatedAt(LocalDateTime.now().toGrpcTimestamp())
                ).build())

        val request = HttpRequest.GET<List<PixInfo>>("/keys/${UUID.randomUUID().toString()}")
        val response = this.httpClient.toBlocking().exchange(request,Any::class.java)

        with(response){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(HttpStatus.OK.code,status.code)
        }

    }

    @Test
    fun `Nao deve listar quando cliente nao existe`(){

        Mockito.`when`(this.grpcKeyManager.listagemChaves(any(ClienteRequest::class.java)))
            .thenThrow(Status.NOT_FOUND.asRuntimeException())

        val throws = assertThrows<HttpClientResponseException> {
            val request = HttpRequest.GET<List<PixInfo>>("/keys/${UUID.randomUUID()}")
            val response = this.httpClient.toBlocking().exchange(request,Any::class.java)
        }

        with(throws){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(HttpStatus.NOT_FOUND.code,status.code)
        }

    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class Clients{
        @Singleton
        fun listStub() = Mockito.mock(KeyManagerListKeysGrpc
            .KeyManagerListKeysBlockingStub::class.java)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
    private fun <T> any(): T = Mockito.any()
}