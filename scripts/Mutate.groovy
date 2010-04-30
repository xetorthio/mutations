import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << grailsScript("Init")
includeTargets << grailsScript("Package")
includeTargets << grailsScript("Bootstrap")

target(main: "Mutate the db to the latests mutation") {
    //first check if there is at least one mutation
    def mutationsDir = new File("${basedir}/grails-app/mutations/")
    if(mutationsDir.listFiles().size()) {
        loadGrailsContext()
        

        def appliedMutations = getAppliedMutations()
        def mutationsCache = [:]
        def availableMutations = getAvailableMutations(mutationsDir, mutationsCache)

        availableMutations.removeAll(appliedMutations)
        availableMutations.plus(appliedMutations)

        if(availableMutations.size()==0) {
            println "The db is already at the last mutation."
            exit(1)
        }
        applyMutations(availableMutations, mutationsCache)
    } else {
        println "The db is already at the last mutation."
        exit(1)
    }
}

def applyMutations(availableMutations, mutationsCache) {
    ClassLoader parent = getClass().getClassLoader();
    GroovyClassLoader loader = new GroovyClassLoader(parent);
    loader.clearCache();
    availableMutations.each {
        Class groovyClass = loader.parseClass(mutationsCache[it]);
        groovyClass.getMetaClass().executeSQL = { sql ->
            getSession().createSQLQuery(sql).executeUpdate()
        }
        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
        println "Applying mutation: ${mutationsCache[it]}"
        groovyObject.up()
        getSession().createSQLQuery("INSERT INTO schema_mutations (mutation) VALUES (${it})").executeUpdate()
    }
}

def getAvailableMutations(mutationsDir, mutationsCache) {
    def availableMutations = []
    mutationsDir.eachFile {
        def mutationsVersion = it.name.split("_")[0]
        availableMutations << mutationsVersion
        mutationsCache[mutationsVersion]=it
    }
    return availableMutations
}

def getAppliedMutations() {
    def session = getSession()
    def mutated
    try {
        mutated = session.createSQLQuery("SELECT mutation FROM schema_mutations").list()
    } catch(org.hibernate.exception.SQLGrammarException ex) {
        println "Creating table schema_mutations"
        session.createSQLQuery("CREATE TABLE schema_mutations (mutation VARCHAR(20) not null, CONSTRAINT pk_mutation PRIMARY KEY(mutation))").executeUpdate()
        mutated = session.createSQLQuery("SELECT mutation FROM schema_mutations").list()
    }
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
