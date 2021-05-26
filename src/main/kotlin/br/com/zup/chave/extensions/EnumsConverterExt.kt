package br.com.zup.chave.extensions

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.dto.ChaveType
import br.com.zup.chave.dto.ContaType


fun TipoConta.convertContaType():ContaType{

    return when(this)
    {
        TipoConta.CONTA_POUPANCA -> ContaType.CONTA_POUPANCA
        else -> ContaType.CONTA_CORRENTE
    }
}

fun TipoChave.convertChaveType():ChaveType{
    return when(this){
        TipoChave.CPF -> ChaveType.CPF
        TipoChave.CELULAR -> ChaveType.CELULAR
        TipoChave.EMAIL -> ChaveType.EMAIL
        else -> ChaveType.ALEATORIO
    }
}

