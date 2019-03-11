package com.avd

import com.avd.application.ApplicationVerticle
import io.vertx.core.Vertx

fun main(args: Array<String>) {
    Vertx.vertx().deployVerticle(ApplicationVerticle::class.java.name)
}