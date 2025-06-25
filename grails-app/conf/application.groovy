def secretsFile = new File('grails-app/conf/application-secrets.yml')

if (secretsFile.exists()) {
    grails.config.locations = ["file:grails-app/conf/application-secrets.yml"]
} else {
    grails.config.locations = []
}
