package es.uvigo.esei.daa.dao;


import static es.uvigo.esei.daa.dataset.PetsDataset.*;
import static es.uvigo.esei.daa.matchers.IsEqualToPet.containsPetsInAnyOrder;
import static es.uvigo.esei.daa.matchers.IsEqualToPet.equalsToPet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;

import es.uvigo.esei.daa.entities.Pet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

import es.uvigo.esei.daa.listeners.ApplicationContextBinding;
import es.uvigo.esei.daa.listeners.ApplicationContextJndiBindingTestExecutionListener;
import es.uvigo.esei.daa.listeners.DbManagement;
import es.uvigo.esei.daa.listeners.DbManagementTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:contexts/mem-context.xml")
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DbManagementTestExecutionListener.class,
        ApplicationContextJndiBindingTestExecutionListener.class
})
@ApplicationContextBinding(
        jndiUrl = "java:/comp/env/jdbc/daaexample",
        type = DataSource.class
)
@DbManagement(
        create = "classpath:db/hsqldb.sql",
        drop = "classpath:db/hsqldb-drop.sql"
)
@DatabaseSetup("/datasets/dataset.xml")
@ExpectedDatabase("/datasets/dataset.xml")
public class PetDAOTest {
    private PetsDAO dao;

    @Before
    public void setUp() throws Exception {
        this.dao = new PetsDAO();
    }

    @Test
    public void testList() throws DAOException {
        assertThat(this.dao.list(), containsPetsInAnyOrder(pets()));
    }

    @Test
    public void testGet() throws DAOException {
        final Pet pet = this.dao.get(existentId());

        assertThat(pet, is(equalsToPet(existentPet())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNonExistentId() throws DAOException {
        this.dao.get(nonExistentId());
    }

    @Test
    @ExpectedDatabase("/datasets/pets/dataset-delete.xml")
    public void testDelete() throws DAOException {
        this.dao.delete(existentId());

        assertThat(this.dao.list(), containsPetsInAnyOrder(petsWithout(existentId())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNonExistentId() throws DAOException {
        this.dao.delete(nonExistentId());
    }

    @Test
    @ExpectedDatabase("/datasets/pets/dataset-modify.xml")
    public void testModify() throws DAOException {
        final Pet pet = existentPet();
        pet.setName(newName());
        pet.setAnimal(newAnimal());
        pet.setBreed(newBreed());
        pet.setOwnerId(newOwner());

        this.dao.modify(pet);

        final Pet persistentPet = this.dao.get(pet.getId());

        assertThat(persistentPet, is(equalsToPet(pet)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyNonExistentId() throws DAOException {
        this.dao.modify(nonExistentPet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyNullPet() throws DAOException {
        this.dao.modify(null);
    }

    @Test
    @ExpectedDatabase("/datasets/people/dataset-add.xml")
    public void testAdd() throws DAOException {
        final Pet pet = this.dao.add(newPet());

        assertThat(pet, is(equalsToPet(newPet())));

        final Pet persistentPet = this.dao.get(pet.getId());

        assertThat(persistentPet, is(equalsToPet(newPet())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullName() throws DAOException {
        this.dao.add(new Pet(1 ,null,"algo","algo", newOwner()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullBreed() throws DAOException {
        this.dao.add(new Pet(1,"algo",null,"algo", 1));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAnimal() throws DAOException {
        this.dao.add(new Pet(1,"algo","algo",null, 1));
    }

}
