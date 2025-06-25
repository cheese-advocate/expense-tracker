package expense

@groovy.transform.ToString()
class Transaction {
    User user

    Date dateCreated  // automatically populated by Grails
    String description
    BigDecimal amountZAR
    BigDecimal runningBalanceZAR

    static constraints = {
        user nullable: false
        description nullable: true, maxSize: 255
        amountZAR scale: 2, min: 0.01G
        runningBalanceZAR scale: 2, min: 0.0G
    }

    static mapping = {
        runningBalanceZAR index: 'running_balance_idx'
    }

    static transients = ['amountUSD', 'runningBalanceUSD']

    def exchangeRateService  // injected service

    BigDecimal getAmountUSD() {
        exchangeRateService?.convertZARtoUSD(amountZAR) ?: 0.0G
    }

    BigDecimal getRunningBalanceUSD() {
        exchangeRateService?.convertZARtoUSD(runningBalanceZAR) ?: 0.0G
    }

    // String toString() {
    //     return "${description ?: 'New Transaction'} - ZAR ${amountZAR ?: '0.00'}"
    // }
}
