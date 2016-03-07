package es.uvigo.esei.daa.dao;


import static es.uvigo.esei.daa.dataset.PetsDataset.*;
import static es.uvigo.esei.daa.matchers.IsEqualToPet.containsPetsInAnyOrder;
import static es.uvigo.esei.daa.matchers.IsEqualToPet.equalsToPet;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.reset;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import es.uvigo.esei.daa.entities.Pet;
import org.junit.Ignore;
import org.junit.Test;

import com.mysql.jdbc.Statement;

import es.uvigo.esei.daa.util.DatabaseQueryUnitTest;

public class PetDAOUnitTest extends DatabaseQueryUnitTest {
    @Test
    public void testList() throws Exception {
        final Pet[] pets = pets();

        for (Pet pet : pets) {
            expectPetRow(pet);
        }
        expect(result.next()).andReturn(false);
        result.close();

        replayAll();
        final PetsDAO petsDAO = new PetsDAO();

        assertThat(petsDAO.list(), containsPetsInAnyOrder(pets));
    }

    @Test(expected = DAOException.class)
    public void testListUnexpectedException() throws Exception {
        expect(result.next()).andThrow(new SQLException());
        result.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.list();
    }

    @Test
    public void testGet() throws Exception {
        final Pet existentPet = existentPet();

        expectPetRow(existentPet);
        result.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();

        assertThat(petsDAO.get(existentId()), is(equalTo(existentPet)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMissing() throws Exception {
        expect(result.next()).andReturn(false);
        result.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.get(existentId());
    }

    @Test(expected = DAOException.class)
    public void testGetUnexpectedException() throws Exception {
        expect(result.next()).andThrow(new SQLException());
        result.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.get(existentId());
    }

    @Test
    public void testAdd() throws Exception {
        final Pet pet = newPet();
        reset(connection);
        expect(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .andReturn(statement);
        expect(statement.executeUpdate()).andReturn(1);
        expect(statement.getGeneratedKeys()).andReturn(result);

        // Key retrieval
        expect(result.next()).andReturn(true);
        expect(result.getInt(1)).andReturn(pet.getId());
        connection.close();
        result.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        final Pet newPet = petsDAO.add(newPet());

        assertThat(newPet, is(equalsToPet(pet)));
    }

    @Ignore
    /*
    * Ignorando porque el contructor de pet ya lanza la excepción si alguno de los parámetros son nulos
    * Preguntar a MrJato
    * */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullName() throws Exception {
        replayAll();

        final PetsDAO petsDAO = new PetsDAO();

        resetAll(); // No expectations

        petsDAO.add(new Pet(1 ,null,"algo","algo", newOwner()));
    }

    @Ignore
    /*
    * Ignorando porque el contructor de pet ya lanza la excepción si alguno de los parámetros son nulos
    * Preguntar a MrJato
    * */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullBreed() throws Exception {
        replayAll();

        final PetsDAO petsDAO = new PetsDAO();

        resetAll(); // No expectations

        petsDAO.add(new Pet(1 ,"algo", null, "algo", newOwner()));
    }

    @Ignore
    /*
    * Ignorando porque el contructor de pet ya lanza la excepción si alguno de los parámetros son nulos
    * Preguntar a MrJato
    * */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAnimal() throws Exception {
        replayAll();

        final PetsDAO petsDAO = new PetsDAO();

        resetAll(); // No expectations

        petsDAO.add(new Pet(1 ,"algo","algo", null, newOwner()));
    }

    @Test(expected = DAOException.class)
    public void testAddZeroUpdatedRows() throws Exception {
        reset(connection);
        expect(connection.prepareStatement(anyString(), eq(1)))
                .andReturn(statement);
        expect(statement.executeUpdate()).andReturn(0);
        connection.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.add(newPet());
    }

    @Test(expected = DAOException.class)
    public void testAddNoGeneratedKey() throws Exception {
        reset(connection);
        expect(connection.prepareStatement(anyString(), eq(1)))
                .andReturn(statement);
        expect(statement.executeUpdate()).andReturn(1);
        expect(statement.getGeneratedKeys()).andReturn(result);
        expect(result.next()).andReturn(false);
        result.close();
        connection.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.add(newPet());
    }

    @Test(expected = DAOException.class)
    public void testAddUnexpectedException() throws Exception {
        reset(connection);
        expect(connection.prepareStatement(anyString(), eq(1)))
                .andReturn(statement);
        expect(statement.executeUpdate()).andThrow(new SQLException());
        connection.close();

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.add(newPet());
    }

    @Test
    public void testDelete() throws Exception {
        expect(statement.executeUpdate()).andReturn(1);

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.delete(existentId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalidId() throws Exception {
        expect(statement.executeUpdate()).andReturn(0);

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.delete(existentId());
    }

    @Test(expected = DAOException.class)
    public void testDeleteUnexpectedException() throws Exception {
        expect(statement.executeUpdate()).andThrow(new SQLException());

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.delete(existentId());
    }

    @Test
    public void testModify() throws Exception {
        expect(statement.executeUpdate()).andReturn(1);

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.modify(existentPet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyNullPet() throws Exception {
        replayAll();

        final PetsDAO petsDAO = new PetsDAO();

        resetAll(); // No expectations

        petsDAO.modify(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyZeroUpdatedRows() throws Exception {
        expect(statement.executeUpdate()).andReturn(0);

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.modify(existentPet());
    }

    @Test(expected = DAOException.class)
    public void testModifyUnexpectedException() throws Exception {
        expect(statement.executeUpdate()).andThrow(new SQLException());

        replayAll();

        final PetsDAO petsDAO = new PetsDAO();
        petsDAO.modify(existentPet());
    }

    private void expectPetRow(Pet pet) throws SQLException {
        expect(result.next()).andReturn(true);
        expect(result.getInt("id")).andReturn(pet.getId());
        expect(result.getString("name")).andReturn(pet.getName());
        expect(result.getString("breed")).andReturn(pet.getBreed());
        expect(result.getString("animal")).andReturn(pet.getAnimal());
        expect(result.getInt("owner_id")).andReturn(pet.getOwnerId());
    }
}
