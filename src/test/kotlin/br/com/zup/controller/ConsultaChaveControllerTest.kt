package br.com.zup.controller

import br.com.zup.KeyManagerSearchGrpc
import br.com.zup.factory.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class ConsultaChaveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcKeyManager : KeyManagerSearchGrpc.KeyManagerSearchBlockingStub


    @AfterEach
    fun cleanUp(){
        Mockito.reset(this.grpcKeyManager)
    }

    @BeforeEach
    fun setup(){
    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class Clients{
        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerSearchGrpc
            .KeyManagerSearchBlockingStub::class.java)
    }
}