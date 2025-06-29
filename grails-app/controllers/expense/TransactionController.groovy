package expense

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType

class TransactionController {

    TransactionService transactionService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        List<Map> txListWithUsd = transactionService.list(params)
        respond txListWithUsd, model: [transactionCount: transactionService.count(), txListWithUsd: txListWithUsd]
        // respond transactionService.list(params), model:[transactionCount: transactionService.count()]
    }

    def show(Long id) {
        respond transactionService.get(id)
    }

    def create() {
        respond new Transaction(params)
    }

    def save(Transaction transaction) {
        if (transaction == null) {
            notFound()
            return
        }

        try {
            transactionService.save(transaction)
        } catch (ValidationException e) {
            respond transaction.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'transaction.label', default: 'Transaction'), transaction.id])
                redirect transaction
            }
            '*' { respond transaction, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond transactionService.get(id)
    }

    def update(Transaction transaction) {
        if (transaction == null) {
            notFound()
            return
        }

        try {
            transactionService.save(transaction)
        } catch (ValidationException e) {
            respond transaction.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'transaction.label', default: 'Transaction'), transaction.id])
                redirect transaction
            }
            '*'{ respond transaction, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        transactionService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'transaction.label', default: 'Transaction'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    /**
     * User must be able to export all their transactions to CSV file
     * Exports all transactions to a CSV file.
     * The CSV will include all transactions, or can be filtered based on parameters.
     */
    def exportCsv() {
        // Get all transactions (or apply filters as needed)
        List<Transaction> transactions = transactionService.list([max: Integer.MAX_VALUE])

        response.setContentType("text/csv")
        response.setHeader("Content-disposition", "attachment; filename=transactions.csv")
        response.characterEncoding = "UTF-8"

        // Write CSV to response output stream
        transactionService.exportToCsv(transactions, response.outputStream)

        response.outputStream.flush()
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'transaction.label', default: 'Transaction'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
