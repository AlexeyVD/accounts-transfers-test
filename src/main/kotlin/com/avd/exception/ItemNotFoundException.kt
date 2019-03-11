package com.avd.exception

import java.lang.Exception

class ItemNotFoundException(private val id: Long) : Exception() {
    override val message: String?
        get() = "Item not found for id = $id"
}