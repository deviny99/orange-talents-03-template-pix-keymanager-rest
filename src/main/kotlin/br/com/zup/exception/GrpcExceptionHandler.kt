package br.com.zup.exception

import com.google.rpc.BadRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
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
class GrpcExceptionHandler : ExceptionHandler<StatusRuntimeException,MutableHttpResponse<StatusRuntimeException>> {

    override fun handle(request: HttpRequest<*>?,
                        exception: StatusRuntimeException): MutableHttpResponse<StatusRuntimeException> {

        val map = mapOf(
            Pair("path",request?.uri),
            Pair("timestamp",LocalDateTime.now().toString()),
            Pair("status",this.convertStatus(exception.status).code),
            Pair("message", exception.status.description) ,
            Pair("fields",exception.violations()),
            Pair("error",this.convertStatus(exception.status))
        )

        val response : MutableHttpResponse<StatusRuntimeException> =
            HttpResponse.status(this.convertStatus(exception.status))
        response.body(map)

        return response

    }

    private fun convertStatus(status: Status): HttpStatus{

       return when(status.code){

            Status.NOT_FOUND.code ->  HttpStatus.NOT_FOUND
            Status.ALREADY_EXISTS.code -> HttpStatus.UNPROCESSABLE_ENTITY
            Status.UNAUTHENTICATED.code-> HttpStatus.UNAUTHORIZED
            Status.INVALID_ARGUMENT.code -> HttpStatus.BAD_REQUEST
            Status.UNKNOWN.code -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
       }
    }

    private fun StatusRuntimeException.violations(): List<Pair<String, String>>? {

        if (StatusProto.fromThrowable(this)?.detailsList!!.isNotEmpty()){
            val details = StatusProto.fromThrowable(this)
                ?.detailsList?.get(0)!!
                .unpack(BadRequest::class.java)

            return details.fieldViolationsList
                .map { it.field to it.description }
        }
           return null
    }

}