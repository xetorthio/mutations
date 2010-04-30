class GrailsMutationsGrailsPlugin {
    def version = 0.1
    def title = "A database migration framework for Grails"
    def grailsVersion = "1.2.2 >"
    def author = "Jonathan Leibiusky"
    def authorEmail = "ionathan@gmail.com"
    def description = "Grails Mutations plugin is a simple solution to the problem of managing incremental changes to the databases over time. It is called Mutation because there are a lot of Migration framework out there and it seems more accurate to say that the DB is being mutated. Each mutation is versioned and tracked in the database itself, so it is easy to detect when it need to be applied."
}
