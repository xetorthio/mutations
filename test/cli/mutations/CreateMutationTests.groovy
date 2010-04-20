package mutations

import grails.test.*
import groovy.mock.interceptor.*

class CreateMutationTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    void testMutationNameIsRequired() {
        execute(["create-mutation"])
        assertEquals 1, waitForProcess()
        assertTrue output.contains("Mutation name not specified.")	
    }
    
    void testMutationFileIsCreated() {
        execute(["create-mutation", "SomeTest"])
        assertEquals 0, waitForProcess()
        assertTrue output.contains("Created new mutation")
        println output
        output.find(/Created new mutation: \s/, { println it })
    }
}
