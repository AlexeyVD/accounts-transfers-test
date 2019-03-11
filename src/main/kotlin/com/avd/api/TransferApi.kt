package com.avd.api

import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router

class TransferApi(router: Router, eventBus: EventBus) : AbstractApi(router, eventBus) {

    override fun applyContract() {
        register("/account/create", Handler { send("account/create", it) }, HttpMethod.PUT)
        register("/account/get", Handler { send("account/get", it) })
        register("/transfer", Handler { send("transfer", it) })
    }
}