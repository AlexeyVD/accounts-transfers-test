package com.avd.api

import io.vertx.ext.web.Router

interface Api {

    /**
     * Initialize api router
     * @return router instance
     */
    fun initRouter(): Router
}