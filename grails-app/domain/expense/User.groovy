import groovy.transform.ToString
import expense.Transaction

@ToString(includeNames = true, includeFields = true)
class User {
    String name
    BigDecimal startingBalanceZAR

    static hasMany = [transactions: Transaction]

    static constraints = {
        name unique: true, blank: false
        startingBalanceZAR scale: 2, min: 0.0G
    }

    String toString() {
        return name
    }

}
