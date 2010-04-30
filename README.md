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

License
-------

Copyright (c) 2010 Jonathan Leibiusky

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

