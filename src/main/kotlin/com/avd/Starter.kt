package com.avd

import com.avd.application.ApplicationVerticle
import io.vertx.core.Vertx

fun main() {
    Vertx.vertx().deployVerticle(ApplicationVerticle::class.java.name)
}