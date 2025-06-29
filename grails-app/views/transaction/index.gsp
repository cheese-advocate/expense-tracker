<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <a href="#list-transaction" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                        <li><g:link action="exportCsv" class="export-csv-btn">Export to CSV</g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="list-transaction" class="col-12 content scaffold-list" role="main">
                    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <!-- <f:table collection="${transactionList}" /> -->
                    <table>
                        <thead>
                            <tr>
                            <th><g:message code="transaction.user.label" default="User"/></th>
                            <th><g:message code="transaction.description.label" default="Description"/></th>
                            <th><g:message code="transaction.amountZAR.label" default="Amount (ZAR)"/></th>
                            <th><g:message code="transaction.runningBalanceZAR.label" default="Running Balance (ZAR)"/></th>
                            <th><g:message code="transaction.amountUSD.label" default="Amount (ZAR)"/></th>
                            <th><g:message code="transaction.runningBalanceUSD.label" default="Running Balance (ZAR)"/></th>
                            <th><g:message code="transaction.dateCreated.label" default="Date Created"/></th>
                            <th><g:message code="default.actions.label" default="Actions"/></th>
                            <th>Amount (USD)</th>
                            <th>Running Balance (USD)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${txListWithUsd}" var="entry">
                            <tr>
                                <td>${entry.transaction.user?.name}</td>
                                <td>${entry.transaction.description}</td>
                                <td>${entry.transaction.amountZAR?.setScale(2, BigDecimal.ROUND_HALF_UP)}</td>
                                <td>${entry.transaction.runningBalanceZAR?.setScale(2, BigDecimal.ROUND_HALF_UP)}</td>
                                <td>${entry.transaction.amountUSD?.setScale(2, BigDecimal.ROUND_HALF_UP)}</td>
                                <td>${entry.transaction.runningBalanceUSD?.setScale(2, BigDecimal.ROUND_HALF_UP)}</td>
                                <td><g:formatDate date="${entry.transaction.dateCreated}" format="yyyy-MM-dd HH:mm"/></td>
                                <td>
                                    <g:link action="show" resource="${entry.transaction}">Show</g:link> |
                                    <g:link action="edit" resource="${entry.transaction}">Edit</g:link>
                                </td>
                            </tr>
                            </g:each>
                        </tbody>
                    </table>

                    <g:if test="${transactionCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${transactionCount ?: 0}" />
                    </div>
                    </g:if>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>