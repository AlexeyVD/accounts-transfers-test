package com.avd.application

import com.avd.api.TransferApi
import com.avd.transaction.TransfersVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class ApplicationVerticle : AbstractVerticle() {

    private lateinit var server: Server

    /**
     * Start http server with transfer restful api
     */
    override fun start() {
        onStart()
        super.start()
    }

    override fun stop() {
        onStop()
        super.stop()
    }

    private fun onStart() {
        vertx.deployVerticle(TransfersVerticle::class.java.name)
        server = Server(vertx.createHttpServer(), TransferApi(Router.router(vertx), vertx.eventBus()))
        server.start()
    }

    private fun onStop() {
        server.stop()
    }
}