package br.com.zup.exception

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
@Produces
class HttpExceptionHandler : ExceptionHandler<HttpException,MutableHttpResponse<HttpException>> {

    override fun handle(request: HttpRequest<*>?,
                        exception: HttpException): MutableHttpResponse<HttpException> {

        val map = mapOf(
            Pair("path",request?.uri),
            Pair("timestamp",LocalDateTime.now().toString()),
            Pair("status",exception.status.code),
            Pair("message",exception.localizedMessage),
            Pair("error",exception.status.reason)
        )

        val response : MutableHttpResponse<HttpException> = HttpResponse.status(exception.status)
        response.body(map)

        return response

    }

}