package es.uvigo.esei.daa.suites;

import es.uvigo.esei.daa.dao.PetDAOTest;
import es.uvigo.esei.daa.rest.PetsResourceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.esei.daa.dao.PeopleDAOTest;
import es.uvigo.esei.daa.rest.PeopleResourceTest;

@SuiteClasses({ 
	PeopleDAOTest.class,
	PeopleResourceTest.class,
	PetDAOTest.class,
	PetsResourceUnitTest.class
})
@RunWith(Suite.class)
public class IntegrationTestSuite {
}
