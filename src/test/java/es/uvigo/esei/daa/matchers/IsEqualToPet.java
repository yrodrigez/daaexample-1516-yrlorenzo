package es.uvigo.esei.daa.matchers;

import es.uvigo.esei.daa.entities.Pet;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * Created by yago on 5/03/16.
 */
public class IsEqualToPet extends IsEqualToEntity<Pet> {
    public IsEqualToPet(Pet entity) {
        super(entity);
    }

    @Override
    protected boolean matchesSafely(Pet actual) {
        this.clearDescribeTo();

        if (actual == null) {
            this.addTemplatedDescription("actual", expected.toString());
            return false;
        } else {
            return checkAttribute("id", Pet::getId, actual)
                    && checkAttribute("name", Pet::getName, actual)
                    && checkAttribute("breed", Pet::getBreed, actual)
                    && checkAttribute("animal", Pet::getAnimal,actual)
                    && checkAttribute("ownerId", Pet::getOwnerId, actual);
        }
    }
    @Factory
    public static IsEqualToPet equalsToPet(Pet pet) {
        return new IsEqualToPet(pet);
    }

    @Factory
    public static Matcher<Iterable<? extends Pet>> containsPetsInAnyOrder(Pet ... pets) {
        return containsEntityInAnyOrder(IsEqualToPet::equalsToPet, pets);
    }
}
