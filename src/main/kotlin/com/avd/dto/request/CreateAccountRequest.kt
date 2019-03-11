package com.avd.dto.request

import com.avd.dto.Dto
import java.math.BigDecimal

class CreateAccountRequest() : Dto {

    lateinit var balance: BigDecimal

    constructor(balance: BigDecimal) : this() {
        this.balance = balance
    }

}