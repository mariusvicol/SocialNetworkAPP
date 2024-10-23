package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    private final String filename;

    /**
     * @param validator - the validator used
     * @param fileName - the file name
     */
    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename=fileName;
        loadData();
    }

    /**
     * Load data from file
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                linie = linie.trim();
                if (!linie.isEmpty()) {
                    E e = createEntity(linie);
                    super.save(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param line - the line to be parsed
     * @return the entity created from the line
     */
    public abstract E createEntity(String line);

    /**
     * @param entity - the entity to be parsed
     * @return the line created from the entity
     */
    public abstract String saveEntity(E entity);

    /**
     * @param id -the id of the entity to be returned id must not be null
     *           id must not be null
     * @return the entity with the specified id
     */
    @Override
    public Optional<E> findOne(ID id) {
        return super.findOne(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    /**
     * @param entity entity must be not null id must not be null
     * @return null- if the given entity is saved
     */
    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.isEmpty())
            writeToFile();
        return e;
    }

    /**
     * Write all entities to file
     */
    private void writeToFile() {

        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param id id must be not null id must exist
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> deletedEntity = super.delete(id); // Use the inherited method from InMemoryRepository
        if (deletedEntity.isPresent()) {
            writeToFile(); // Update the file after deletion
        }
        return deletedEntity;
    }

    /**
     * @param entity entity must not be null id must not be null
     * @return the updated entity
     */
    @Override
    public Optional<E> update(E entity) {
        Optional<E> updatedEntity = super.update(entity); // Use the inherited method from InMemoryRepository
        if (updatedEntity.isEmpty()) {
            writeToFile(); // Update the file after the update
        }
        return updatedEntity;
    }
}
