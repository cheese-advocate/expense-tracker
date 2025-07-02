package expense

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class TransactionSpec extends Specification implements DomainUnitTest<Transaction> {

    def setup() {
    }

    def cleanup() {
    }

    void "test valid transaction passes validation"() {
        given:
        def user = new User(name: 'testuser', startingBalanceZAR: 500.00G)
        mockDomain(User, [user]) // Needed for user reference if not mocked elsewhere

        def tx = new Transaction(
            user: user,
            description: 'Groceries',
            amountZAR: 100.00G,
            runningBalanceZAR: 500.00G
        )

        expect:
        tx.validate()
        tx.errors.errorCount == 0
    }

    void "test constraints: amountZAR must be at least 0.01"() {
        given:
        def tx = new Transaction(
            user: new User(name: 'testuser', startingBalanceZAR: 500.00G),
            amountZAR: 0.00G,
            runningBalanceZAR: 100.00G
        )

        expect:
        !tx.validate()
        tx.errors['amountZAR']?.code == 'min.notmet'
    }

    void "test constraints: runningBalanceZAR cannot be negative"() {
        given:
        def tx = new Transaction(
            user: new User(name: 'testuser', startingBalanceZAR: 500.00G),
            amountZAR: 100.00G,
            runningBalanceZAR: -10.00G
        )

        expect:
        !tx.validate()
        tx.errors['runningBalanceZAR']?.code == 'min.notmet'
    }

    void "test amountUSD is calculated using exchangeRateService"() {
        given:
        def tx = new Transaction(
            user: new User(name: 'testuser', startingBalanceZAR: 500.00G),
            amountZAR: 100.00G,
            runningBalanceZAR: 200.00G
        )
        tx.exchangeRateService = Mock(ExchangeRateService) {
            getZARtoUSD(100.00G) >> 6.00G
        }

        expect:
        tx.amountUSD == 6.00G
    }

    void "test runningBalanceUSD is calculated using exchangeRateService"() {
        given:
        def tx = new Transaction(
            user: new User(name: 'testuser', startingBalanceZAR: 500.00G),
            amountZAR: 100.00G,
            runningBalanceZAR: 200.00G
        )
        tx.exchangeRateService = Mock(ExchangeRateService) {
            getZARtoUSD(200.00G) >> 12.00G
        }

        expect:
        tx.runningBalanceUSD == 12.00G
    }

    void "test toString falls back to default when description is null"() {
        given:
        def tx = new Transaction(amountZAR: 50.00G)

        expect:
        tx.toString() == "New Transaction - ZAR 50.00"
    }
}
