package mutations

import grails.test.*
import groovy.mock.interceptor.*

class MutateTests extends AbstractCliTestCase {
    def sessionFactory

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

    void testMutateWhenMutationIsAvailable() {
        createMutation "CreateNewTable", '\nexecuteSQL "CREATE TABLE atable (afield varchar(255))"', '\nexecuteSQL "DROP TABLE atable"'
        execute(["mutate"])
        assertEquals 0, waitForProcess()
        assertTrue output.contains("Applying mutation:")
        sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO atable(afield) values ('somevalue')").executeUpdate()
        def results = sessionFactory.getCurrentSession().createSQLQuery("SELECT * FROM atable").list()
        assertEquals 1, results.size()
        output.find(/Created new mutation:\s(.*)/, { matched, fileName ->
                def file = new File(fileName)
                assert file.exists()
                file.delete()
            })
    }

    void createMutation(name, contentUP, contentDOWN) {
        execute(["create-mutation", name])
        assertEquals 0, waitForProcess()
        output.find(/Created new mutation:\s(.*)/, { match, fileName ->
                def file = new File(fileName)
                assert file.exists()
                file.text = file.text.replaceFirst(/\s*\/\/\s*UP content goes here/, contentUP)
                file.text = file.text.replaceFirst(/\s*\/\/\s*DOWN content goes here/, contentDOWN)
            })
    }
}
