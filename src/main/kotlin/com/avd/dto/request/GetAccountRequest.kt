package com.avd.dto.request

import com.avd.dto.Dto

class GetAccountRequest(): Dto {

    var id: Long = 0

    constructor(id: Long) : this() {
        this.id = id
    }
}