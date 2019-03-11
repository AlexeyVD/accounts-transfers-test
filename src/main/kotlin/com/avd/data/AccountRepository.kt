package com.avd.data

import com.avd.exception.ItemNotFoundException
import com.avd.model.Account
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class AccountRepository : Repository<Account> {

    private val accountsInc = AtomicLong()
    private val accounts = ConcurrentHashMap<Long, Account>()

    override fun get(id: Long) = accounts[id] ?: throw ItemNotFoundException(id)

    override fun change(item: Account) {
        accounts[item.id()] = item
    }

    override fun delete(id: Long) {
        accounts.remove(id)
    }

    override fun items() = accounts.values

    fun createAccount(balance: BigDecimal): Long {
        val account = Account(accountsInc.incrementAndGet(), balance)
        accounts[account.id()] = account
        return  account.id()
    }
}