def secretsFile = new File('grails-app/conf/application-secrets.yml')
if (secretsFile.exists()) {
    if (!grails.config.locations) {
        grails.config.locations = []
    }
    grails.config.locations += ["file:" + secretsFile.path]
}
