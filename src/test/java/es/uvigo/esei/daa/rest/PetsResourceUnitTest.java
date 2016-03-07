package es.uvigo.esei.daa.rest;

import static es.uvigo.esei.daa.dataset.PeopleDataset.existentPerson;
import static es.uvigo.esei.daa.dataset.PetsDataset.*;
import static es.uvigo.esei.daa.matchers.HasHttpStatus.hasBadRequestStatus;
import static es.uvigo.esei.daa.matchers.HasHttpStatus.hasInternalServerErrorStatus; 
import static es.uvigo.esei.daa.matchers.HasHttpStatus.hasOkStatus; 
import static es.uvigo.esei.daa.matchers.IsEqualToPet.containsPetsInAnyOrder; 
import static es.uvigo.esei.daa.matchers.IsEqualToPet.equalsToPet; 
import static java.util.Arrays.asList; 
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject; 
import static org.easymock.EasyMock.anyString; 
import static org.easymock.EasyMock.createMock; 
import static org.easymock.EasyMock.expect; 
import static org.easymock.EasyMock.expectLastCall; 
import static org.easymock.EasyMock.replay; 
import static org.easymock.EasyMock.verify; 
import static org.hamcrest.CoreMatchers.is; 
import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertThat;
 
import java.util.List;
 
import javax.ws.rs.core.Response;

import es.uvigo.esei.daa.dataset.PeopleDataset;
import es.uvigo.esei.daa.entities.Person;
import org.junit.After;
import org.junit.Before; 
import org.junit.Test;
 
import es.uvigo.esei.daa.dao.DAOException; 
import es.uvigo.esei.daa.dao.PetsDAO; 
import es.uvigo.esei.daa.entities.Pet;

/**
 * Created by yago on 6/03/16.
 */ 
public class PetsResourceUnitTest {
    private PetsDAO daoMock;
    private PetsResource resource;

    @Before
    public void setUp() throws Exception {
        daoMock = createMock(PetsDAO.class);
        resource = new PetsResource(daoMock);
    }

    @After
    public void tearDown() throws Exception {
        try {
            verify(daoMock);
        } finally {
            daoMock = null;
            resource = null;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testList() throws Exception {
        final List<Pet> pets = asList(pets());

        expect(daoMock.list()).andReturn(pets);

        replay(daoMock);

        final Response response = resource.list();

        assertThat(response, hasOkStatus());
        assertThat((List<Pet>) response.getEntity(), containsPetsInAnyOrder(pets()));
    }

    @Test
    public void testListDAOException() throws Exception {
        expect(daoMock.list()).andThrow(new DAOException());

        replay(daoMock);

        final Response response = resource.list();

        assertThat(response, hasInternalServerErrorStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPersonsPests() throws Exception{
        final List<Pet> pets = asList(pets());
        final Person person = existentPerson();
        expect(daoMock.personsPets(person.getId())).andReturn(pets);

        replay(daoMock);

        final Response response = resource.personsPets(person.getId());

        assertThat(response, hasOkStatus());
        assertThat((List<Pet>) response.getEntity(), containsPetsInAnyOrder(pets()));
    }

    @Test
    public void testPersonsPestsDAOException() throws Exception{
        expect(daoMock.personsPets(existentPerson().getId())).andThrow(new DAOException());

        replay(daoMock);

        final Response response = resource.personsPets(existentPerson().getId());

        assertThat(response, hasInternalServerErrorStatus());
    }


    @Test
    public void testPersonsPetsIllegalArgumentException() throws Exception {
        expect(daoMock.personsPets(anyInt())).andThrow(new IllegalArgumentException());

        replay(daoMock);

        final Response response = resource.personsPets(PeopleDataset.nonExistentId());

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testDelete() throws Exception {
        daoMock.delete(anyInt());

        replay(daoMock);

        final Response response = resource.delete(1);

        assertThat(response, hasOkStatus());
    }

    @Test
    public void testDeleteDAOException() throws Exception {
        daoMock.delete(anyInt());
        expectLastCall().andThrow(new DAOException());

        replay(daoMock);

        final Response response = resource.delete(1);

        assertThat(response, hasInternalServerErrorStatus());
    }

    @Test
    public void testDeleteIllegalArgumentException() throws Exception {
        daoMock.delete(anyInt());
        expectLastCall().andThrow(new IllegalArgumentException());
        replay(daoMock);

        final Response response = resource.delete(1);

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testModify() throws Exception {
        final Pet pet = existentPet();
        pet.setName(newName());
        pet.setBreed(newBreed());

        daoMock.modify(pet);

        replay(daoMock);

        final Response response = resource.modify(
                pet.getId(), pet.getName(), pet.getBreed(), pet.getAnimal(), pet.getOwnerId());

        assertThat(response, hasOkStatus());
        assertEquals(pet, response.getEntity());
    }

    @Test
    public void testModifyDAOException() throws Exception {
        daoMock.modify(anyObject());
        expectLastCall().andThrow(new DAOException());

        replay(daoMock);

        final Response response = resource.modify(
                existentId(), newName(), newBreed(), newAnimal(), newOwner()
        );

        assertThat(response, hasInternalServerErrorStatus());
    }

    @Test
    public void testModifyIllegalArgumentException() throws Exception {
        daoMock.modify(anyObject());
        expectLastCall().andThrow(new IllegalArgumentException());

        replay(daoMock);

        final Response response = resource.modify(
                existentId(), newName(), newBreed(), newAnimal(), newOwner()
        );

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testAdd() throws Exception {
        expect(daoMock.add(newPet()))
                .andReturn(newPet());
        replay(daoMock);


        final Response response = resource.add(
                newName(), newBreed(), newAnimal(), newOwner()
        );

        assertThat(response, hasOkStatus());
        assertThat((Pet) response.getEntity(), is(equalsToPet(newPet())));
    }

    @Test
    public void testAddDAOException() throws Exception {
        expect(daoMock.add(anyObject()))
                .andThrow(new DAOException());
        replay(daoMock);

        final Response response = resource.add(
                newName(), newBreed(), newAnimal(), newOwner()
        );

        assertThat(response, hasInternalServerErrorStatus());
    }

    @Test
    public void testAddIllegalArgumentException() throws Exception {
        expect(daoMock.add(anyObject()))
                .andThrow(new IllegalArgumentException());
        replay(daoMock);

        final Response response = resource.add(
                newName(), newBreed(), newAnimal(), newOwner()
        );

        assertThat(response, hasBadRequestStatus());
    }
}
