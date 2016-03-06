package es.uvigo.esei.daa.entities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PetUnitTest {
    @Test
    public void testPetIntStringStringStringInt() {
        final int id = 1;
        final int ownerId = 1;
        final String name = "Huguito";
        final String breed = "Chávez";
        final String animal = "Chabestia";

        final Pet pet = new Pet(id, name, breed, animal, ownerId);

        assertThat(pet.getId(), is(equalTo(id)));
        assertThat(pet.getName(), is(equalTo(name)));
        assertThat(pet.getBreed(), is(equalTo(breed)));
        assertThat(pet.getAnimal(), is(equalTo(animal)));
        assertThat(pet.getOwnerId(), is(equalTo(ownerId)));
    }

    @Test(expected = NullPointerException.class)
    public void testPetIntStringStringNullName() {
        new Pet(1, null, "Chávez", "Chabestia", 1);
    }

    @Test(expected = NullPointerException.class)
    public void testPetIntStringStringNullBreed() {
        new Pet(1, "Huguito", null, "Chabestia", 1);
    }

    @Test(expected = NullPointerException.class)
    public void testPetIntStringStringNullAnimal() { new Pet(1, "Huguito", "Chávez", null, 1); }

    @Test
    public void testSetName() {
        final int id = 1;
        final String breed = "Isturiz";
        final String animal = "Chabestia";
        final int ownerId = 1;

        final Pet pet = new Pet(id, "Aristóbulo", breed, animal, ownerId);
        pet.setName("Negrito");

        assertThat(pet.getId(), is(equalTo(id)));
        assertThat(pet.getName(), is(equalTo("Negrito")));
        assertThat(pet.getBreed(), is(equalTo(breed)));
        assertThat(pet.getAnimal(), is(equalTo(animal)));
        assertThat(pet.getOwnerId(), is(equalTo(ownerId)));

    }

    @Test(expected = NullPointerException.class)
    public void testSetNullName() {
        final Pet pet = new Pet(1, "Huguito", "Chávez", "Chabestia", 1);

        pet.setName(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullBreed() {
        final Pet pet = new Pet(1, "Huguito", "Chávez", "Chabestia", 1);

        pet.setBreed(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullAnimal() {
        final Pet pet = new Pet(1, "Huguito", "Chávez", "Chabestia", 1);

        pet.setAnimal(null);
    }

    @Test
    public void testSetBreed() {
        final int id = 1;
        final String name = "Huguito";
        final String animal= "Chabestia";
        final int ownerId = 1;

        final Pet pet = new Pet(1, name, "Castro", animal, ownerId);
        pet.setBreed("Chávez");

        assertThat(pet.getId(), is(equalTo(id)));
        assertThat(pet.getName(), is(equalTo(name)));
        assertThat(pet.getBreed(), is(equalTo("Chávez")));
        assertThat(pet.getAnimal(), is(equalTo(animal)));
        assertThat(pet.getOwnerId(), is(equalTo(ownerId)));
    }

    @Test
    public void testSetAnimal() {
        final int id = 1;
        final String name = "Huguito";
        final String breed= "Chávez";
        final int ownerId = 1;

        final Pet pet = new Pet(1, name, breed, "Socialista", ownerId);
        pet.setAnimal("Chabestia");

        assertThat(pet.getId(), is(equalTo(id)));
        assertThat(pet.getName(), is(equalTo(name)));
        assertThat(pet.getBreed(), is(equalTo(breed)));
        assertThat(pet.getAnimal(), is(equalTo("Chabestia")));
        assertThat(pet.getOwnerId(), is(equalTo(ownerId)));
    }

    @Test
    public void testEqualsObject() {
        final Pet chavez = new Pet(1, "Huguito", "Chávez", "Chabestia", 1);
        final Pet aristobulo = new Pet(1, "Aristóbulo", "Isturiz", "Chabestia", 2);

        assertTrue(chavez.equals(aristobulo));
    }

}
