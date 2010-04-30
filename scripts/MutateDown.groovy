import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << grailsScript("Init")
includeTargets << grailsScript("Package")
includeTargets << grailsScript("Bootstrap")

target(main: "Mutate the db to the previous mutation") {
    //first check if there is at least one mutation
    def mutationsDir = new File("${basedir}/grails-app/mutations/")
    if(mutationsDir.listFiles().size()) {
        loadGrailsContext()
        
        def existingMutations = getExistingMutations(mutationsDir)
        def lastMutation = getLastMutation()

        if(!lastMutation) {
            println "There is no previous mutation."
            exit(1)
        }
        rollbackMutation(lastMutation, existingMutations)
    } else {
        println "There is no previous mutation."
        exit(1)
    }
}

def rollbackMutation(lastMutation, existingMutations) {
    ClassLoader parent = getClass().getClassLoader();
    GroovyClassLoader loader = new GroovyClassLoader(parent);
    loader.clearCache();
    Class groovyClass = loader.parseClass(existingMutations[lastMutation]);
    groovyClass.getMetaClass().executeSQL = { sql ->
        getSession().createSQLQuery(sql).executeUpdate()
    }
    GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
    println "Rollback mutation: ${existingMutations[lastMutation]}"
    groovyObject.down()
    getSession().createSQLQuery("DELETE FROM schema_mutations WHERE mutation = ${lastMutation}").executeUpdate()
}

def getExistingMutations(mutationsDir) {
    def existingMutations = [:]
    mutationsDir.eachFile {
        def mutationsVersion = it.name.split("_")[0]
        existingMutations[mutationsVersion]=it
    }
    return existingMutations
}

def getLastMutation() {
    def session = getSession()
    def mutation = null
    try {
        mutation = session.createSQLQuery("SELECT MAX(mutation) FROM schema_mutations").list().get(0)
    } catch(org.hibernate.exception.SQLGrammarException ex) {
        println ex
    }
    return mutation
}

def getSession() {
	def sessionFactory = appCtx.getBean("sessionFactory")
    def session = sessionFactory.getCurrentSession()
}

def loadGrailsContext() {
    compile()
    compilePlugins()
    classLoader = new URLClassLoader([classesDir.toURL()] as URL[], rootLoader)
    Thread.currentThread().setContextClassLoader(classLoader)
    loadApp()
    configureApp()
    configureHibernateSession()
}

def configureHibernateSession() {
	// without this you'll get a lazy initialization exception when using a many-to-many relationship
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = SessionFactoryUtils.getSession(sessionFactory, true)
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
}

setDefaultTarget(main)
