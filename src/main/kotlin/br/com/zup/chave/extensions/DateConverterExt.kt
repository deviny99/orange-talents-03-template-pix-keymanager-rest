package br.com.zup.chave.extensions


import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


fun LocalDateTime.toGrpcTimestamp(): Timestamp{
        val instant = this.atZone(ZoneId.of("UTC")).toInstant()
        return Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }

fun Timestamp.toLocalDateTime(): LocalDateTime{
    val instant = Instant.ofEpochSecond(this.seconds,this.nanos.toLong())
    return LocalDateTime.ofInstant(instant, ZoneId.of("America/Sao_Paulo"))
}


