package br.com.zup.chave.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class PixInfo(val chaveType: ChaveType,
                   val pixValue:String,
                   val clienteId:String,
                   val tipoConta: ContaType,
                   val pixId:String,
                   @field:JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") val createAt:LocalDateTime)

