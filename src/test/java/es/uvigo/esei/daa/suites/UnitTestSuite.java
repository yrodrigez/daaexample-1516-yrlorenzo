package es.uvigo.esei.daa.suites;

import es.uvigo.esei.daa.dao.PetDAOTest;
import es.uvigo.esei.daa.dao.PetDAOUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.esei.daa.dao.PeopleDAOUnitTest;
import es.uvigo.esei.daa.entities.PersonUnitTest;
import es.uvigo.esei.daa.rest.PeopleResourceUnitTest;

@SuiteClasses({
	PersonUnitTest.class,
	PeopleDAOUnitTest.class,
	PeopleResourceUnitTest.class,
	PetDAOTest.class,
	PetDAOUnitTest.class
})
@RunWith(Suite.class)
public class UnitTestSuite {
}
