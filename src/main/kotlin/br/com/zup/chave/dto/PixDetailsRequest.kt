package br.com.zup.chave.dto

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class PixDetailsRequest(@field:NotBlank val pixId:String,
                             @field:NotBlank val clienteId:String) {
}