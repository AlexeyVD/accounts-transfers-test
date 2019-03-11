package com.avd.transaction

import com.avd.application.ApplicationVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import spock.lang.Specification
import spock.util.concurrent.AsyncConditions

class TransfersVerticleTest extends Specification {

    def setupSpec() {
        Vertx.vertx().deployVerticle(ApplicationVerticle.class.name)
    }

    def cleanupSpec() {
        Vertx.vertx().close()
    }

    def "Create account"() {
        given:
        def client = WebClient.wrap(Vertx.vertx().createHttpClient())
        def async = new AsyncConditions(1)
        def req = new JsonObject().put("balance", 100L)

        when:
        client.put(8080, "localhost", "/account/create")
                .sendJson(req, { res ->
                    async.evaluate({
                        assert res.succeeded()
                        assert res.result().bodyAsJsonObject().containsKey("id")
                    })
                })

        then:
        async.await()
    }

    def "Get account"() {
        given:
        def client = WebClient.wrap(Vertx.vertx().createHttpClient())
        def async = new AsyncConditions(2)
        def createReq = new JsonObject().put("balance", 100L)


        when:
        client.put(8080, "localhost", "/account/create")
                .sendJson(createReq, { createRes ->
                    async.evaluate({
                        assert createRes.succeeded()

                        def id = createRes.result().bodyAsJsonObject().getLong("id")
                        def getReq = new JsonObject().put("id", id)

                        client.post(8080, "localhost", "/account/get")
                                .sendJson(getReq, { getRes ->
                                    async.evaluate({
                                        assert getRes.succeeded()
                                        assert getRes.result().bodyAsJsonObject().getLong("id") == id
                                        assert getRes.result().bodyAsJsonObject().getLong("balance") == 100
                                    })
                                })
                    })
                })

        then:
        async.await()
    }


    def "Check transfer with generated data from TransfersVerticle"() {
        given:
        def client = WebClient.wrap(Vertx.vertx().createHttpClient())
        def async = new AsyncConditions(3)
        def req = new JsonObject()
                .put("from", 1)
                .put("to", 3)
                .put("amount", 10L)
        def getReq1 = new JsonObject().put("id", 1)
        def getReq3 = new JsonObject().put("id", 3)

        when:
        client.post(8080, "localhost", "/transfer")
                .sendJson(req, { transferRes ->
                    async.evaluate({
                        assert transferRes.succeeded()
                    })

                    client.post(8080, "localhost", "/account/get")
                            .sendJson(getReq1, { getRes ->
                                async.evaluate({
                                    assert getRes.succeeded()
                                    assert getRes.result().bodyAsJsonObject().getLong("id") == 1
                                    assert getRes.result().bodyAsJsonObject().getLong("balance") == 90
                                })
                            })

                    client.post(8080, "localhost", "/account/get")
                            .sendJson(getReq3, { getRes ->
                                async.evaluate({
                                    assert getRes.succeeded()
                                    assert getRes.result().bodyAsJsonObject().getLong("id") == 3
                                    assert getRes.result().bodyAsJsonObject().getLong("balance") == 110
                                })
                            })
                })

        then:
        async.await()
    }
}
