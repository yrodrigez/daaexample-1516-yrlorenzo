package es.uvigo.esei.daa.dataset;

import es.uvigo.esei.daa.entities.Pet;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.Arrays.binarySearch;
import static java.util.Arrays.stream;


public class PetsDataset {
    private PetsDataset() {}

    public static Pet[] personsPets() {
        return new Pet[] {
                new Pet(
                        5,
                        "chacho",
                        "carasuc",
                        "loro",
                        5
                )
        };
    }

    public static Pet[] pets() {
        return new Pet[] {
                new Pet(1, "Whisky", "Beagle", "Dog", 1),
                new Pet(2, "Perroman", "Doberman", "Dog", 4),
                new Pet(3, "Perro2", "Siames", "Cat", 1),
                new Pet(4, "Perro3", "Gray", "Rhino", 2),
                new Pet(5, "chacho", "carasuc", "loro", 5)
        };

    }

    public static Pet[] emptyPetsCollection() {
        return new Pet[] {} ;
    }

    public static Pet[] petsWithout(int ... ids) {
        Arrays.sort(ids);

        final Predicate<Pet> hasValidId = pet ->
                binarySearch(ids, pet.getId()) < 0;

        return stream(pets())
                .filter(hasValidId)
                .toArray(Pet[]::new);
    }

    public static Pet pet(int id) {
        return stream(pets())
                .filter(pet -> pet.getId() == id)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static int existentId() {
        return 3;
    }

    public static int nonExistentId() {
        return 1234;
    }

    public static Pet existentPet() {
        return pet(existentId());
    }

    public static Pet nonExistentPet() {
        return new Pet(nonExistentId(), "Jane", "Smith", "animal", nonExistentId());
    }

    public static String newName() {
        return "unrino";
    }

    public static String newBreed() {
        return "gray";
    }

    public static String newAnimal() { return "rhino"; }

    public static int newOwner() { return 1; }

    public static Pet newPet() {
        return new Pet(6, newName(), newBreed(), newAnimal(), newOwner());
    }
}
