package br.com.zup.exception

import io.micronaut.http.HttpStatus

class HttpException(msg: String,
                    val status:HttpStatus):
    RuntimeException(msg) {

    companion object {

        fun notFound(message:String):HttpException{
            return HttpException(message,HttpStatus.NOT_FOUND)
        }

        fun unprocessable(message:String):HttpException{
            return HttpException(message,HttpStatus.UNPROCESSABLE_ENTITY)
        }

    }

}