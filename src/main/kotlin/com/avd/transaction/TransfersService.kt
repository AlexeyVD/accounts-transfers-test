package com.avd.transaction

import com.avd.data.AccountRepository
import com.avd.exception.ItemNotFoundException
import com.avd.exception.NotEnoughFundsException
import java.math.BigDecimal

class TransfersService(private val repository: AccountRepository) {

    /**
     * Transfer money between [from] and [to] accounts
     */
    @Synchronized
    @Throws(ItemNotFoundException::class, NotEnoughFundsException::class)
    fun transfer(from: Long, to: Long, amount: BigDecimal) {

        if (from == to)
            return

        val fromAcc = repository.get(from)
        val toAcc = repository.get(to)

        val firstLock = if (from < to) fromAcc else toAcc
        val secondLock = if (from > to) fromAcc else toAcc

        synchronized(firstLock) {
            synchronized(secondLock) {
                if (fromAcc.getBalance() < amount) {
                    throw NotEnoughFundsException(fromAcc.getBalance(), amount)
                }
                fromAcc.withdrawal(amount)
                toAcc.deposit(amount)
            }
        }
    }

    /**
     * @return return total balance of existing accounts
     */
    fun totalBalance(): BigDecimal {
        synchronized(this) {
            return repository.items().asSequence()
                .map { it.getBalance() }
                .reduce { val1, val2 -> val1 + val2 }
        }

    }

}