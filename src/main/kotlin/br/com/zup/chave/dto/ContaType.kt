package br.com.zup.chave.dto

import br.com.zup.TipoConta

enum class ContaType(val convertProto: TipoConta) {

    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA);
}