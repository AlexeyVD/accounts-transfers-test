package com.avd.exception

import java.lang.Exception
import java.math.BigDecimal

class NotEnoughFundsException(
    private val balance: BigDecimal,
    private val requestedFunds: BigDecimal
) : Exception() {
    override val message: String?
        get() = "Not enough funds. Balance = $balance, requested funds = $requestedFunds"
}