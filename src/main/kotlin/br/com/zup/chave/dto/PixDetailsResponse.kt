package br.com.zup.chave.dto

import br.com.zup.PixDetalhes
import br.com.zup.chave.extensions.convertChaveType
import br.com.zup.chave.extensions.convertContaType
import br.com.zup.chave.extensions.toLocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class PixDetailsResponse(
    val chaveType: ChaveType,
    val pixValue:String,
    val clienteId:String?,
    val pixId:String?,
    @field:JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") val createAt: LocalDateTime,
    val instituicao: Instituicao,
    val titular: Titular,


    ) {

    constructor(pixDetalhes: PixDetalhes): this(
        chaveType = pixDetalhes.tipoChave.convertChaveType(),
            pixValue = pixDetalhes.pixValue,
            clienteId = pixDetalhes.clienteId,
            pixId = pixDetalhes.pixId,
            createAt = pixDetalhes.createdAt.toLocalDateTime(),
            instituicao = Instituicao(ispb = pixDetalhes.instituicao.ispb,
                agencia = pixDetalhes.instituicao.agencia,
                numero = pixDetalhes.instituicao.numero,
                contaType = pixDetalhes.instituicao.tipoConta.convertContaType()),
            titular = Titular(pixDetalhes.titular.nome,pixDetalhes.titular.cpf)
    )



    data class Instituicao(val ispb:String,
                           val agencia:String,
                           val numero:String,
                           val contaType: ContaType)

    data class Titular(val nome:String,
                       val cpf:String)
}