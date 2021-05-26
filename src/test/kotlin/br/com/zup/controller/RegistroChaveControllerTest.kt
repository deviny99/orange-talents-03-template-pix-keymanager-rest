package br.com.zup.controller

import br.com.zup.*
import br.com.zup.chave.dto.ChavePixRequest
import br.com.zup.chave.dto.ChaveType
import br.com.zup.chave.dto.ContaType
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
class RegistroChaveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcKeyManager : KeyManagerRegistryGrpc.KeyManagerRegistryBlockingStub

    private lateinit var chavePixRequest: ChavePixRequest

    @AfterEach
    fun cleanUp(){
        Mockito.reset(this.grpcKeyManager)
    }

    @BeforeEach
    fun setup(){
        //implementar ChavePixRequest
        this.chavePixRequest = ChavePixRequest(UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            ChaveType.ALEATORIO,
            ContaType.CONTA_CORRENTE)
    }

    @Test
    fun `Deve registrar chave`(){

        Mockito.`when`(this.grpcKeyManager.cadastrarChave(any(ChaveRequest::class.java)))
            .thenReturn(ChaveResponse.newBuilder().setId(UUID.randomUUID().toString()).build())

        val request = HttpRequest.POST("/keys",this.chavePixRequest)
        val response = this.httpClient.toBlocking().exchange(request,Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED,response.status)
        Assertions.assertTrue(response.headers.contains("Location"))

    }

    @Test
    fun `Nao deve registrar chave duplicada`(){

        Mockito.`when`(this.grpcKeyManager.cadastrarChave(Mockito.any()))
            .thenThrow(Status.ALREADY_EXISTS.asRuntimeException())

        val throws = assertThrows<HttpClientResponseException> {
            val request = HttpRequest.POST("/keys",this.chavePixRequest)
            val response = this.httpClient.toBlocking().exchange(request,Any::class.java)
            fail("Não era para cadastrar")
        }

        with(throws){
            println(this.status)
            Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,this.status)
        }

    }

    @Test
    fun `Nao deve registrar chave com dados invalidos`(){

        Mockito.`when`(this.grpcKeyManager.cadastrarChave(Mockito.any()))
            .thenThrow(Status.INVALID_ARGUMENT.asRuntimeException())

        val throws = assertThrows<HttpClientResponseException> {
            val request = HttpRequest.POST("/keys",this.chavePixRequest)
            val response = this.httpClient.toBlocking().exchange(request,Any::class.java)
            fail("Não era para cadastrar")
        }

        with(throws){
            Assertions.assertEquals(HttpStatus.BAD_REQUEST,this.status)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class Clients{
        @Singleton
        fun stubRegistra() = Mockito.mock(KeyManagerRegistryGrpc
            .KeyManagerRegistryBlockingStub::class.java)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
    private fun <T> any(): T = Mockito.any()
}