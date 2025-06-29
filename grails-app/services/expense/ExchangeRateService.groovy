package expense

import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.springframework.beans.factory.InitializingBean

/**
 * Service for fetching and caching exchange rates between ZAR and USD using the Fixer.io API.
 */
@Transactional
class ExchangeRateService implements GrailsApplicationAware, InitializingBean {

    GrailsApplication grailsApplication
    String apiKey
    BigDecimal fallbackRate = 0.55G

    private BigDecimal cachedRate
    private Date lastFetchTime
    private final int cacheExpiryMinutes = 5

    @Override
    void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
    }

    void afterPropertiesSet() {
        apiKey = System.getenv('fixer_io_api_key') ?:
                 grailsApplication?.config?.getProperty('api.fixer_io_api_key', String)
    }

    BigDecimal getZARtoUSD(BigDecimal amountZAR) {
        BigDecimal rate = getCachedOrFreshRate()
        return (amountZAR * rate).setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    private synchronized BigDecimal getCachedOrFreshRate() {
        if (cachedRate && lastFetchTime) {
            def minutesElapsed = (System.currentTimeMillis() - lastFetchTime.time) / 1000 / 60
            if (minutesElapsed < cacheExpiryMinutes) {
                return cachedRate
            }
        }

        BigDecimal freshRate = fetchExchangeRate()
        if (freshRate) {
            cachedRate = freshRate
            lastFetchTime = new Date()
        }

        return cachedRate ?: fallbackRate
    }

    private BigDecimal fetchExchangeRate() {
        if (!apiKey) {
            return null
        }

        try {
            def url = new URL("http://data.fixer.io/api/latest?access_key=${apiKey}&base=EUR&symbols=USD,ZAR")
            def connection = url.openConnection()
            connection.setConnectTimeout(3000)
            connection.setReadTimeout(3000)

            def response = new JsonSlurper().parse(connection.inputStream)
            if (response?.success && response.rates?.USD && response.rates?.ZAR) {
                BigDecimal rateUSD = new BigDecimal(response.rates.USD.toString())
                BigDecimal rateZAR = new BigDecimal(response.rates.ZAR.toString())
                return (rateUSD / rateZAR).setScale(6, BigDecimal.ROUND_HALF_UP)
            }
        } catch (Exception e) {
            log.warn("Fixer.io API call failed: ${e.message}")
        }

        return null
    }
}
