import grails.mutations.Mutation

class SomeTest extends Mutation {
    void up() {
        println 'UP'
    }

    void down() {
        println 'DOWN'
    }
}
