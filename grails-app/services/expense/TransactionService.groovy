package expense

import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import expense.ExchangeRateService

/**
 * Service class for managing transactions, including CRUD operations and CSV export.
 */
@Transactional
class TransactionService {

    ExchangeRateService exchangeRateService

    Transaction get(Serializable id) {
        def tx = Transaction.get(id)
        if (tx != null) {
            tx.exchangeRateService = this.exchangeRateService
        }
        return tx
    }

    /**
     * Returns a list of transactions decorated with amountUSD and runningBalanceUSD values.
     * Accepts params same as the original list() method for pagination/sorting.
     */
    List<Map> list(Map params) {
        List<Transaction> transactions = Transaction.list(params)

        transactions.collect { tx ->
            tx.exchangeRateService = this.exchangeRateService // Inject the exchange rate service into each transaction
            [
                transaction: tx,
                amountUSD: tx.amountUSD,
                runningBalanceUSD: tx.runningBalanceUSD
            ]
        }
    }

    Long count() {
        Transaction.count()
    }

    void delete(Serializable id) {
        def transaction = Transaction.get(id)
        if (transaction) {
            transaction.delete(flush: true)
        }
    }

    Transaction save(Transaction transaction) {
        if (!transaction.user) {
            throw new ValidationException('User is required', transaction.errors)
        }

        // Override any tampered value submitted by the user
        transaction.runningBalanceZAR = null

        // Determine previous balance: last transaction or user's starting balance
        def lastTransaction = Transaction.findByUser(
            transaction.user,
            [sort: 'dateCreated', order: 'desc']
        )

        BigDecimal previousBalance = lastTransaction?.runningBalanceZAR ?: transaction.user?.startingBalanceZAR ?: 0.0G
        transaction.runningBalanceZAR = previousBalance - transaction.amountZAR

        // Save with flush and failOnError for safety
        transaction.save(flush: true, failOnError: true)
    }

    void exportToCsv(List<Map> transactions, OutputStream outputStream) {
        def formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        .withZone(ZoneId.systemDefault())

        outputStream.withWriter(StandardCharsets.UTF_8.name()) { writer ->
            writer.write(
                'User,Description,Amount (ZAR),Amount (USD),' +
                'Running Balance (ZAR),Running Balance (USD),Date Created\n'
            )

            transactions.each { tx ->
                def transaction = tx.transaction
                String userName = transaction.user?.name?.replaceAll(/"/, '""') ?: ''
                String description = (transaction.description ?: '').replaceAll(/"/, '""')
                String dateCreated = transaction.dateCreated ? formatter.format(transaction.dateCreated.toInstant()) : ''
                String amountUSD = tx.amountUSD?.setScale(2, BigDecimal.ROUND_HALF_UP)
                String balanceUSD = tx.runningBalanceUSD?.setScale(2, BigDecimal.ROUND_HALF_UP)
                String amountZAR = transaction.amountZAR?.setScale(2, BigDecimal.ROUND_HALF_UP)
                String runningBalanceZAR = transaction.runningBalanceZAR?.setScale(2, BigDecimal.ROUND_HALF_UP)

                writer.write(
                    "\"${userName}\",\"${description}\"," +
                    "${amountZAR},${amountUSD},${runningBalanceZAR},${balanceUSD},\"${dateCreated}\"\n"
                )
            }
        }
    }
}
