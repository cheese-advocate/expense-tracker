def secretsFile = new File('secrets.properties')
if (secretsFile.exists()) {
    if (!grails.config.locations) {
        grails.config.locations = []
    }
    grails.config.locations += ['file:' + secretsFile.absolutePath]
    println "Secrets properties file loaded from ${secretsFile.absolutePath}"
}
