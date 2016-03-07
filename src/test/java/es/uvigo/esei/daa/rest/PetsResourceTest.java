package es.uvigo.esei.daa.rest;

        import static es.uvigo.esei.daa.dataset.PetsDataset.existentId;
        import static es.uvigo.esei.daa.dataset.PetsDataset.existentPet;
        import static es.uvigo.esei.daa.dataset.PetsDataset.newName;
        import static es.uvigo.esei.daa.dataset.PetsDataset.newPet;
        import static es.uvigo.esei.daa.dataset.PetsDataset.newBreed;
        import static es.uvigo.esei.daa.dataset.PetsDataset.newAnimal;
        import static es.uvigo.esei.daa.dataset.PetsDataset.newOwner;
        import static es.uvigo.esei.daa.dataset.PetsDataset.nonExistentId;
        import static es.uvigo.esei.daa.dataset.PetsDataset.pets;
        import static es.uvigo.esei.daa.matchers.HasHttpStatus.hasBadRequestStatus;
        import static es.uvigo.esei.daa.matchers.HasHttpStatus.hasOkStatus;
        import static es.uvigo.esei.daa.matchers.IsEqualToPet.containsPetsInAnyOrder;
        import static es.uvigo.esei.daa.matchers.IsEqualToPet.equalsToPet;
        import static javax.ws.rs.client.Entity.entity;
        import static org.hamcrest.CoreMatchers.equalTo;
        import static org.hamcrest.CoreMatchers.is;
        import static org.junit.Assert.assertThat;

        import java.io.IOException;
        import java.util.List;

        import javax.sql.DataSource;
        import javax.ws.rs.client.Entity;
        import javax.ws.rs.core.Application;
        import javax.ws.rs.core.Form;
        import javax.ws.rs.core.GenericType;
        import javax.ws.rs.core.MediaType;
        import javax.ws.rs.core.Response;

        import org.glassfish.jersey.client.ClientConfig;
        import org.glassfish.jersey.test.JerseyTest;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.test.context.ContextConfiguration;
        import org.springframework.test.context.TestExecutionListeners;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

        import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
        import com.github.springtestdbunit.DbUnitTestExecutionListener;
        import com.github.springtestdbunit.annotation.DatabaseSetup;
        import com.github.springtestdbunit.annotation.ExpectedDatabase;

        import es.uvigo.esei.daa.DAAExampleApplication;
        import es.uvigo.esei.daa.entities.Pet;
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

/**
 * Created by yago on 6/03/16.
 */
public class PetsResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new DAAExampleApplication();
    }

    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config);

        // Enables JSON transformation in client
        config.register(JacksonJsonProvider.class);
        config.property("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE);
    }

    @Test
    public void testList() throws IOException {
        final Response response = target("pets").request().get();
        assertThat(response, hasOkStatus());

        final List<Pet> pets = response.readEntity(new GenericType<List<Pet>>(){});

        assertThat(pets, containsPetsInAnyOrder(pets()));
    }

    @Test
    public void testGet() throws IOException {
        final Response response = target("pets/" + existentId()).request().get();
        assertThat(response, hasOkStatus());

        final Pet pet = response.readEntity(Pet.class);

        assertThat(pet, is(equalsToPet(existentPet())));
    }

    @Test
    public void testGetInvalidId() throws IOException {
        final Response response = target("pets/" + nonExistentId()).request().get();

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    @ExpectedDatabase("/datasets/pets/dataset-add.xml")
    public void testAdd() throws IOException {
        final Form form = new Form();
        form.param("name", newName());
        form.param("breed", newBreed());
        form.param("animal", newAnimal());
        form.param("ownerId", String.valueOf(newOwner()));


        final Response response = target("pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        assertThat(response, hasOkStatus());

        final Pet pet = response.readEntity(Pet.class);

        assertThat(pet, is(equalsToPet(newPet())));
    }

    @Test
    public void testAddMissingName() throws IOException {
        final Form form = new Form();
        form.param("breed", newBreed());
        form.param("animal", newAnimal());
        form.param("ownerId", String.valueOf(newOwner()));

        final Response response = target("pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testAddMissingBreed() throws IOException {
        final Form form = new Form();
        form.param("name", newName());
        form.param("animal", newAnimal());
        form.param("ownerId", String.valueOf(newOwner()));

        final Response response = target("pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testAddMissingAnimal() throws IOException {
        final Form form = new Form();
        form.param("name", newName());
        form.param("breed", newBreed());
        form.param("ownerId", String.valueOf(newOwner()));

        final Response response = target("pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    @ExpectedDatabase("/datasets/pets/dataset-modify.xml")
    public void testModify() throws IOException {
        final Form form = new Form();
        form.param("name", newName());
        form.param("breed", newBreed());
        form.param("animal", newAnimal());
        form.param("ownerId", String.valueOf(newOwner()));

        final Response response = target("pets/" + existentId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        assertThat(response, hasOkStatus());

        final Pet modifiedPet = response.readEntity(Pet.class);

        final Pet pet = existentPet();
        pet.setName(newName());
        pet.setBreed(newBreed());

        assertThat(modifiedPet, is(equalsToPet(pet)));
    }

    @Test
    public void testModifyName() throws IOException {
        final Form form = new Form();
        form.param("name", newName());

        final Response response = target("pets/" + existentId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testModifyBreed() throws IOException {
        final Form form = new Form();
        form.param("breed", newBreed());

        final Response response = target("pets/" + existentId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testModifyAnimal() throws IOException {
        final Form form = new Form();
        form.param("animal", newAnimal());

        final Response response = target("pets/" + existentId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    public void testModifyInvalidId() throws IOException {
        final Form form = new Form();
        form.param("name", newName());
        form.param("breed", newBreed());
        form.param("animal", newAnimal());
        form.param("ownerId", String.valueOf(newOwner()));

        final Response response = target("pets/" + nonExistentId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response, hasBadRequestStatus());
    }

    @Test
    @ExpectedDatabase("/datasets/pets/dataset-delete.xml")
    public void testDelete() throws IOException {
        final Response response = target("pets/" + existentId()).request().delete();

        assertThat(response, hasOkStatus());

        final Integer deletedId = response.readEntity(Integer.class);

        assertThat(deletedId, is(equalTo(existentId())));
    }

    @Test
    public void testDeleteInvalidId() throws IOException {
        final Response response = target("pets/" + nonExistentId()).request().delete();

        assertThat(response, hasBadRequestStatus());
    }
}
