package es.uvigo.esei.daa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.daa.entities.Pet;


public class PetsDAO extends DAO {
    private final static Logger LOG = Logger.getLogger(PetsDAO.class.getName());


    public Pet get(int id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM pets WHERE id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a pet", e);
            throw new DAOException(e);
        }
    }

    public List<Pet> list() throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM pets";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Pet> pets = new LinkedList<>();

                    while (result.next()) {
                        pets.add(rowToEntity(result));
                    }

                    return pets;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing pets", e);
            throw new DAOException(e);
        }
    }

    public List<Pet> personsPets(int personId) throws DAOException {
        try(final Connection conn = this.getConnection()){
            final String query = "SELECT pets.* FROM pets WHERE pets.owner_id= ?";

            try(final PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1,personId);
                try(final ResultSet result = stmt.executeQuery()) {
                    final List<Pet> pets = new LinkedList<>();
                    while (result.next()) {
                        pets.add(rowToEntity(result));
                    }
                    return pets;
                }
            }
        } catch (SQLException e){
            LOG.log(Level.SEVERE, "Error personsPets", e);
            throw new DAOException(e);
        }
    }

    public Pet add(Pet pet)
            throws DAOException, IllegalArgumentException {
        if (pet==null) {
            throw new IllegalArgumentException("Pet is null!");
        }

        try (Connection conn = this.getConnection()) {
            final String query = "INSERT INTO pets(id, owner_id, name, breed, animal) VALUES (null,?,?,?,?)";

            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, pet.getOwnerId());
                statement.setString(2, pet.getName());
                statement.setString(3, pet.getBreed());
                statement.setString(4, pet.getAnimal());

                if (statement.executeUpdate() == 1) {
                    try (ResultSet resultKeys = statement.getGeneratedKeys()) {
                        if (resultKeys.next()) {
                            pet.setId(resultKeys.getInt(1));
                            return pet;
                        } else {
                            LOG.log(Level.SEVERE, "Error retrieving inserted id");
                            throw new SQLException("Error retrieving inserted id");
                        }
                    }
                } else {
                    LOG.log(Level.SEVERE, "Error inserting value");
                    throw new SQLException("Error inserting value");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error adding a pet", e);
            throw new DAOException(e);
        }
    }


    public void modify(Pet pet)
            throws DAOException, IllegalArgumentException {
        if (pet == null) {
            throw new IllegalArgumentException("pet can't be null");
        }

        try (Connection conn = this.getConnection()) {
            final String query = "UPDATE pets SET name=?, breed=?, animal=?, owner_id=? WHERE id=?";

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, pet.getName());
                statement.setString(2, pet.getBreed());
                statement.setString(3, pet.getAnimal());
                statement.setInt(4,pet.getOwnerId());
                statement.setInt(5,pet.getId());

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("name and breed and animal can't be null");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error modifying a pet", e);
            throw new DAOException();
        }
    }


    public void delete(int id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "DELETE FROM pets WHERE id= ?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid id");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting pet: "+ id, e);
            throw new DAOException(e);
        }
    }

    private Pet rowToEntity(ResultSet row) throws SQLException {
        return new Pet(
                row.getInt("id"),
                row.getString("name"),
                row.getString("breed"),
                row.getString("animal"),
                row.getInt("owner_id")
        );
    }
}
