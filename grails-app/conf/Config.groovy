def secretsFile = new File(new File('.').canonicalFile, 'secrets.properties')
if (secretsFile.exists()) {
    println "Secrets properties file found at ${secretsFile.absolutePath}"
    if (!grails.config.locations) grails.config.locations = []
    grails.config.locations << "file:${secretsFile.absolutePath}"
    println "Secrets properties file registered"
}
