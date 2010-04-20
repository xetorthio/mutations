package grails.mutations

abstract class Mutation {
    abstract void up();
    abstract void down();
}
