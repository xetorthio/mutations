includeTargets << grailsScript("Init")

target(main: "Mutate the db to the latests mutation") {
    
    ClassLoader parent = getClass().getClassLoader();
    GroovyClassLoader loader = new GroovyClassLoader(parent);
    Class groovyClass = loader.parseClass(new File("grails-app/mutations/20100419144048792_una_prueba_copada.groovy"));
    GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
    Object[] args = {};
    groovyObject.up()  
}

setDefaultTarget(main)
