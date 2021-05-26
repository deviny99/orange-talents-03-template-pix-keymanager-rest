package br.com.zup.chave.dto

import br.com.zup.TipoChave

enum class ChaveType(val convertProto:TipoChave) {

    ALEATORIO(TipoChave.ALEATORIO),
    EMAIL(TipoChave.EMAIL),
    CELULAR(TipoChave.CELULAR),
    CPF(TipoChave.CPF);

}