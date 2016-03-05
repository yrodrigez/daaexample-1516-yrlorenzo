package es.uvigo.esei.daa.entities;

import static java.util.Objects.requireNonNull;

public class Pet {
    private int id;
    private String name;
    private String breed;
    private String animal;
    private int ownerId;

    // Constructor needed for the JSON conversion
    Pet() {}

    /**
     * Constructs a new instance of {@link Person}.
     *
     * @param id identifier of the pet.
     * @param name name of the person.
     * @param breed breed of the pet.
     * @param ownerId identifier of the pet's owner
     * @param animal references the animal, if it's a dog or a cat or a rhino
     */
    public Pet(
       int id,
       String name,
       String breed,
       String animal,
       int ownerId
    ){
        this.id = id;
        this.ownerId = ownerId;
        this.breed = breed;
        this.name = name;
        this.animal = animal;

    }

    /**
     * Returns the identifier of the pet.
     *
     * @return the identifier of the pet.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = requireNonNull(id, "id can't be null, you clown!"); }

    /**
     * Returns the name of the pet.
     *
     * @return the name of the pet.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this pet.
     *
     * @param name the new name of the pet.
     * @throws NullPointerException if the {@code name} is {@code null}.
     */
    public void setName(String name) {
        this.name = requireNonNull(name, "Name can't be null");
    }

    /**
     * Returns the breed of the pet.
     *
     * @return the breed of the pet.
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Set the breed of this pet.
     *
     * @param breed the new breed of the pet.
     * @throws NullPointerException if the {@code breed} is {@code null}.
     */
    public void setBreed(String breed) {
        this.breed = requireNonNull(breed, "breed can't be null");
    }

    /**
     * Returns the animal type of the pet.
     *
     * @return the animal type of the pet.
     */
    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal)
    {
        this.animal= requireNonNull(animal, "animal can't be null!");
    }
    /**
     * Returns the OwnerId the person who owns this pet.
     *
     * @return the OwnerId the person who owns this pet.
     */
    public int getOwnerId() { return ownerId; }

    public void setOwnerId(int ownerId)
    {
        this.ownerId= requireNonNull(ownerId, "ownerId can't be null!");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pet other = (Pet) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
