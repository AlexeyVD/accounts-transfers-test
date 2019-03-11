package com.avd.transaction

import com.avd.data.AccountRepository
import com.avd.exception.ItemNotFoundException
import com.avd.exception.NotEnoughFundsException
import com.avd.model.Account
import spock.lang.Specification
import spock.util.concurrent.AsyncConditions

import java.util.concurrent.ThreadLocalRandom

class TransfersServiceTest extends Specification {

    AccountRepository repository
    TransfersService transfersService

    def setup() {
        repository = new AccountRepository()
        repository.change(new Account(1, new BigDecimal(100)))
        repository.change(new Account(2, new BigDecimal(100)))
        repository.change(new Account(3, new BigDecimal(100)))
        repository.change(new Account(4, new BigDecimal(100)))
        repository.change(new Account(5, new BigDecimal(100)))

        transfersService = new TransfersService(repository)
    }

    def "Simple transfer"() {

        when:
        transfersService.transfer(1, 2, new BigDecimal(50))

        then:
        repository.get(1).balance == new BigDecimal(50)
        repository.get(2).balance == new BigDecimal(150)

    }

    def "Deadlock between two accounts"() {
        given:
        def async = new AsyncConditions(2)
        def thread1 = new Thread({
            transfersService.transfer(1, 2, new BigDecimal(70))
            async.evaluate({
                assert transfersService.totalBalance() == new BigDecimal(500)
            })
        })
        def thread2 = new Thread({
            transfersService.transfer(2, 1, new BigDecimal(10))
            async.evaluate({
                assert transfersService.totalBalance() == new BigDecimal(500)
            })
        })

        when:
        thread1.start()
        thread2.start()

        then:
        async.await()
    }

    def "Multi thread transfers"() {
        given:
        def threadsCount = 100000
        def async = new AsyncConditions(threadsCount)

        when:

        for (def i = 0; i < threadsCount; i++) {
            def from = ThreadLocalRandom.current().nextInt(1, 6)
            def to = ThreadLocalRandom.current().nextInt(1, 6)
            def amount = new BigDecimal(ThreadLocalRandom.current().nextInt(1, 101))
            new Thread({
                try {
                    transfersService.transfer(from, to, amount)
                } catch(Exception e) {
                    System.out.println(e.getMessage())
                }

                //Check that total balance did not changed after transfer
                async.evaluate({
                    assert transfersService.totalBalance() == new BigDecimal(500)
                })
            }).start()
        }

        then:
        async.await()
        assert transfersService.totalBalance() == new BigDecimal(500)
    }

    def "ItemNotFountException"() {
        when:
        transfersService.transfer(1, 20, new BigDecimal(20))

        then:
        thrown ItemNotFoundException
    }

    def "NotEnoughFundsException"() {
        when:
        transfersService.transfer(1, 2, new BigDecimal(2000))

        then:
        thrown NotEnoughFundsException
    }
}
