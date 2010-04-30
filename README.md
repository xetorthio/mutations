# Mutations

Grails Mutations plugin is a simple solution to the problem of managing incremental changes to the databases over time. It is called Mutation because there are a lot of Migration framework out there and it seems more accurate to say that the DB is being mutated. Each mutation is versioned and tracked in the database itself, so it is easy to detect when it need to be applied.

## Using

In order to install this plugin just type:

    grails install-plugin mutations

To create a new mutation (a unit of change), just type:
    grails create-mutation SomeChange

This will generate a file in your grails-app/mutations folder. The name of the file is the timestamp + lowercase of the mutation name.

You should edit that file and add code to the up() method and down() method. The up() method will be executed to mutate your database. The down() method will be executed to revert a mutation.

To mutate your database to the last mutation just type:
    grails mutate


And to revert the last applied mutation type:
    grails mutate-down

