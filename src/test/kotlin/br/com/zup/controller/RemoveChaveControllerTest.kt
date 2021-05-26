package br.com.zup.controller

import br.com.zup.ChaveRemovidaResponse
import br.com.zup.ClienteChaveRequest
import br.com.zup.KeyManagerDeleteGrpc
import br.com.zup.chave.dto.RemoveChaveRequest
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
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class RemoveChaveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcKeyManager : KeyManagerDeleteGrpc.KeyManagerDeleteBlockingStub

    private lateinit var removeChaveRequest: RemoveChaveRequest

    @AfterEach
    fun cleanUp(){
        Mockito.reset(this.grpcKeyManager)
    }

    @BeforeEach
    fun setup(){
        this.removeChaveRequest = RemoveChaveRequest(UUID.randomUUID().toString(),UUID.randomUUID().toString())
    }

    @Test
    fun `Deve remover chave`(){

        Mockito.`when`(this.grpcKeyManager.removerChave(any(ClienteChaveRequest::class.java)))
            .thenReturn(ChaveRemovidaResponse.newBuilder().setMessage("Chave removida").build())

        val request = HttpRequest.DELETE("/keys",this.removeChaveRequest)
        val response = this.httpClient.toBlocking().exchange(request,Any::class.java)

        with(response){
           Assertions.assertEquals(HttpStatus.OK.code,this.status.code)
        }

    }

    @Test
    fun `Nao deve remover chave que nao existe`(){

        Mockito.`when`(this.grpcKeyManager.removerChave(any()))
            .thenThrow(Status.NOT_FOUND.asRuntimeException())

        val throws = assertThrows<HttpClientResponseException> {
            val request = HttpRequest.DELETE("/keys",this.removeChaveRequest)
            val response = this.httpClient.toBlocking().exchange(request,Any::class.java)

            fail("Não deveria deletar")
        }

        with(throws){
            Assertions.assertEquals(HttpStatus.NOT_FOUND,this.status)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class Clients{
        @Singleton
        fun stubDelete() = Mockito.mock(KeyManagerDeleteGrpc
            .KeyManagerDeleteBlockingStub::class.java)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
    private fun <T> any(): T = Mockito.any()
}