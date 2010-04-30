includeTargets << grailsScript("Init")

target(main: "Create a database mutation") {
    depends(parseArguments)
    
    if(!argsMap["params"]) {
        println "Mutation name not specified."
    	exit(1)
    }
 
    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

    className = argsMap.get("params").get(0)

    fileName = currentTimestamp.toString().replaceAll(/[\s|:|\.|\-]/, { it = '' }) + className.replaceAll(/[A-Z]/, { it = '_' + it }).toLowerCase() + ".groovy"
    File dir = new File("${basedir}/grails-app/mutations/")
    File scriptFile = new File(dir, fileName)
    scriptFile << "import grails.mutations.Mutation\n\nclass ${className} extends Mutation {\n    void up() {\n        // UP content goes here\n    }\n\n    void down() {\n        // DOWN content goes here\n    }\n}\n"
    println "Created new mutation: " + scriptFile
}

setDefaultTarget(main)
