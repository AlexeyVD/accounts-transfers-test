package com.avd.model

import com.avd.exception.NotEnoughFundsException
import java.math.BigDecimal

data class Account(
    private val id: Long,
    private var balance: BigDecimal = BigDecimal.ZERO
) : Model {

    override fun id() = id

    @Synchronized
    fun deposit(value: BigDecimal) {
        balance += value
    }

    @Synchronized
    @Throws(NotEnoughFundsException::class)
    fun withdrawal(value: BigDecimal) {

        if (balance < value)
            throw NotEnoughFundsException(balance, value)

        balance -= value
    }

    @Synchronized
    fun getBalance() = balance
}