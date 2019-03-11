package com.avd.ext

import com.avd.api.APPLICATION_JSON
import com.avd.api.CONTENT_TYPE
import com.avd.dto.Dto
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

fun RoutingContext.response(json: String) {
    response()
        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
        .end(json)
}

fun RoutingContext.response(dto: Dto) {
    response()
        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
        .end(dto.toJson())
}

/**
 * Decode json body from request to Dto class of type [T]
 */
inline fun <reified T : Dto> RoutingContext.body(): T = Json.decodeValue(this.bodyAsString, T::class.java)