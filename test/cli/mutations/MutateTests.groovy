package mutations

import grails.test.*
import groovy.mock.interceptor.*

class MutateTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    void testNothingHappenWhenNoMutation() {
        execute(["mutate"])
        assertEquals 1, waitForProcess()
        assertTrue output.contains("The db is already at the last mutation.")	
    }
}
