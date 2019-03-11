package com.avd.dto.request

import com.avd.dto.Dto
import java.math.BigDecimal

class TransferRequest() : Dto {
    var from: Long = 0
    var to: Long = 0
    lateinit var amount: BigDecimal

    constructor(from: Long, to: Long, amount: BigDecimal) : this() {
        this.from = from
        this.to = to
        this.amount = amount
    }
}
