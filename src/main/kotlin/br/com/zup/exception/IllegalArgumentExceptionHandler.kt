package br.com.zup.exception

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
@Produces
class IllegalArgumentExceptionHandler : ExceptionHandler<IllegalArgumentException, MutableHttpResponse<HttpException>> {

    override fun handle(
        request: HttpRequest<*>?,
        exception: IllegalArgumentException,
    ): MutableHttpResponse<HttpException> {

        val map = mapOf(
            Pair("path", request?.uri),
            Pair("timestamp", LocalDateTime.now().toString()),
            Pair("status", HttpStatus.BAD_REQUEST.code),
            Pair("message", exception.localizedMessage),
            Pair("error", HttpStatus.BAD_REQUEST)
        )

        val response: MutableHttpResponse<HttpException> = HttpResponse.status(HttpStatus.BAD_REQUEST)
        response.body(map)

        return response

    }

}