package com.avd.api

import com.avd.ext.response
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

abstract class AbstractApi(private val router: Router, private val eventBus: EventBus) : Api {

    /**
     * Describe and register api contract
     */
    abstract fun applyContract()

    override fun initRouter(): Router {
        applyContract()
        return router
    }

    /**
     * Declare api method
     * @param path method path
     * @param handler api method handler
     * @param method http method type
     */
    fun register(path: String, handler: Handler<RoutingContext>, method: HttpMethod = HttpMethod.POST) {
        router.route(method, path)
            .handler(BodyHandler.create())
            .consumes(APPLICATION_JSON)
            .produces(APPLICATION_JSON)
            .handler(handler)
    }

    /**
     * Send message to event bus
     * @param address event bus address
     * @param context routing context of request
     */
    fun send(address: String, context: RoutingContext) {
        eventBus.send<String>(address, context.bodyAsString) { handleReply(it, context) }
    }

    private fun handleReply(async: AsyncResult<Message<String>>, context: RoutingContext) {

        if (async.failed()) {
            context.fail(async.cause())
            return
        }

        context.response(async.result().body())
    }
}