package com.avd.transaction

import com.avd.BaseVerticle
import com.avd.data.AccountRepository
import com.avd.dto.AccountDto
import com.avd.dto.request.CreateAccountRequest
import com.avd.dto.request.GetAccountRequest
import com.avd.dto.request.TransferRequest
import com.avd.exception.ItemNotFoundException
import com.avd.ext.typedBody
import com.avd.model.Account
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import java.math.BigDecimal

class TransfersVerticle : BaseVerticle() {

    private val repository = AccountRepository()
    private val transfersService = TransfersService(repository)

    /**
     * Generate some test data
     */
    init {
        repository.createAccount(BigDecimal(100))
        repository.createAccount(BigDecimal(100))
        repository.createAccount(BigDecimal(100))
        repository.createAccount(BigDecimal(100))
        repository.createAccount(BigDecimal(100))
    }

    override fun start() {
        localConsumer("account/create", Handler{ createAccount(it) })
        localConsumer("account/get", Handler{ getAccount(it) })
        localConsumer("transfer", Handler{ executeTransfer(it) })
        super.start()
    }

    /**
     * Handle create account request
     */
    private fun createAccount(message: Message<String>) {
        val body: CreateAccountRequest? = message.typedBody()

        if (body == null) {
            message.fail(400, "")
            return
        }

        message.reply(JsonObject().put("id", repository.createAccount(body.balance)).toString())
    }

    /**
     * Handle get account request
     */
    private fun getAccount(message: Message<String>) {
        val body: GetAccountRequest? = message.typedBody()

        if (body == null) {
            message.fail(400, "")
            return
        }

        val account: Account

        try {
            account = repository.get(body.id)
        } catch (e: ItemNotFoundException) {
            message.fail(404, e.message)
            return
        }
        message.reply(AccountDto(account.id(), account.getBalance()).toJson())
    }

    /**
     * Handle transfer request
     */
    private fun executeTransfer(message: Message<String>) {
        val body: TransferRequest? = message.typedBody()

        if (body == null) {
            message.fail(400, "")
            return
        }

        transfersService.transfer(body.from, body.to, body.amount)

        message.reply(JsonObject().toString())
    }
}