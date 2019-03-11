package com.avd

import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import kotlin.reflect.KFunction

abstract class BaseVerticle : AbstractVerticle() {

    private val consumers = ArrayList<MessageConsumer<*>>()

    override fun stop() {
        unregisterConsumers()
        super.stop()
    }

    protected fun localConsumer(address: String, handler: Handler<Message<String>>) {
        addConsumer(vertx.eventBus().localConsumer<String>(address, handler))
    }

    private fun addConsumer(consumer: MessageConsumer<*>) {
        consumers.add(consumer)
    }

    private fun unregisterConsumers() {
        consumers.forEach { it.unregister() }
    }
}