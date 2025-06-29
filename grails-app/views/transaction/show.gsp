<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <a href="#show-transaction" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="show-transaction" class="col-12 content scaffold-show" role="main">
                    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <dl>
                        <dt><g:message code="transaction.user.label" default="User" /></dt>
                        <dd>${transaction.user?.name}</dd>

                        <dt><g:message code="transaction.description.label" default="Description" /></dt>
                        <dd>${transaction.description}</dd>

                        <dt><g:message code="transaction.amountZAR.label" default="Amount (ZAR)" /></dt>
                        <dd><g:formatNumber number="${transaction.amountZAR}" format="###,##0.00" /></dd>

                        <dt><g:message code="transaction.runningBalanceZAR.label" default="Running Balance (ZAR)" /></dt>
                        <dd><g:formatNumber number="${transaction.runningBalanceZAR}" format="###,##0.00" /></dd>

                        <dt><g:message code="transaction.amountUSD.label" default="Amount (USD)" /></dt>
                        <dd><g:formatNumber number="${transaction.amountUSD}" format="###,##0.00" /></dd>

                        <dt><g:message code="transaction.runningBalanceUSD.label" default="Running Balance (USD)" /></dt>
                        <dd><g:formatNumber number="${transaction.runningBalanceUSD}" format="###,##0.00" /></dd>

                        <dt><g:message code="transaction.dateCreated.label" default="Date Created" /></dt>
                        <dd><g:formatDate date="${transaction?.dateCreated}" format="yyyy-MM-dd HH:mm" /></dd>
                    </dl>

                    <!-- <f:display bean="transaction" /> -->
                    <!-- <dl>
                        <dt><g:message code="transaction.dateCreated.label" default="Date Created"/></dt>
                        <dd><g:formatDate date="${transaction?.dateCreated}" format="yyyy-MM-dd HH:mm" /></dd>
                    </dl> -->
                    <g:form resource="${this.transaction}" method="DELETE">
                        <fieldset class="buttons">
                            <g:link class="edit" action="edit" resource="${this.transaction}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                            <g:set var="deleteLabel"><g:message code="default.button.delete.label" default="Delete" /></g:set>
                            <g:set var="confirmMessage"><g:message code="default.button.delete.confirm.message" default="Are you sure?" /></g:set>
                            <input class="delete" type="submit"
                                value="${deleteLabel}"
                                onclick="return confirm('${confirmMessage}');" />
                        </fieldset>
                    </g:form>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
