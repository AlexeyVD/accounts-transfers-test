package com.avd.dto

import io.vertx.core.json.Json

interface Dto {
    fun toJson() = Json.encodePrettily(this)
}