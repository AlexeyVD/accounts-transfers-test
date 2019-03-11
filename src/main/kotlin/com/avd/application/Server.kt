package com.avd.application

import com.avd.api.Api
import io.vertx.core.http.HttpServer
import org.jetbrains.annotations.NotNull

class Server(@NotNull private val server: HttpServer, private val api: Api) {

    companion object {
        const val PORT = 8080
    }

    /**
     * Start server on [PORT]
     */
    fun start() {
        server.requestHandler(api.initRouter())
            .listen(PORT)
    }

    /**
     * Stop server
     */
    fun stop() {
        server.close()
    }
}