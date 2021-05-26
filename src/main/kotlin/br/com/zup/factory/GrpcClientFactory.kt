package br.com.zup.factory

import br.com.zup.KeyManagerDeleteGrpc
import br.com.zup.KeyManagerListKeysGrpc
import br.com.zup.KeyManagerRegistryGrpc
import br.com.zup.KeyManagerSearchGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
open class GrpcClientFactory(@GrpcChannel("key-manager") private val channel: ManagedChannel){

    @Singleton
    fun registrarChave() = KeyManagerRegistryGrpc.newBlockingStub(channel)

    @Singleton
    fun deletarChave() = KeyManagerDeleteGrpc.newBlockingStub(channel)

    @Singleton
    fun consultarChave() = KeyManagerSearchGrpc.newBlockingStub(channel)

    @Singleton
    fun listarChaves() = KeyManagerListKeysGrpc.newBlockingStub(channel)

}