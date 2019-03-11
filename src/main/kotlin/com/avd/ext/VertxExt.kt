package com.avd.ext

import com.avd.dto.Dto
import io.vertx.core.eventbus.Message
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json

inline fun <reified T : Dto> Message<String>.typedBody(): T? {

    val result: T

    try {
        result = Json.decodeValue(body().toString(), T::class.java)
    } catch (e: DecodeException) {
        println(e.message)
        return null
    }

    return result
}