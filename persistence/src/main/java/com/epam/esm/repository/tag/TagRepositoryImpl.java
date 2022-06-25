package com.epam.esm.repository.tag;

import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.UnknownDataBaseException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates a new instance of an entity in the database.
     * @param  tagEntity -> Object to create
     * @return optional id of created object
     */
    @Override
    public TagEntity create(TagEntity tagEntity) {
        entityManager.persist(tagEntity);
        if (tagEntity.getId() != null)
            return tagEntity;
        throw new UnknownDataBaseException("there was a problem while creating gift certificate. Try again");
    }

    /**
     * Gets all existing entities with provided type and provided limit and offset.
     * @param limit limit of entities
     * @param offset offset for the entities
     * @return list of entities
     */
    @Override
    public List<TagEntity> getAll(int limit, int offset) {
        return entityManager
                .createQuery(GET_ALL, TagEntity.class)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    /**
     * Gets entity with the provided id.
     * @param id id of the needed object
     * @return optional object of provided type
     */
    @Override
    public Optional<TagEntity> findById(Long id) {
        TagEntity tagEntity = entityManager.find(TagEntity.class, id);
        if (tagEntity != null)
            return Optional.of(tagEntity);
        return Optional.empty();
    }

    /**
     * Deletes entity with the provided id.
     * @param id id of the object to be deleted
     * @return the number of rows affected
     */
    @Override
    public int delete(Long id) {
        return entityManager
                .createQuery(DELETE_BY_ID)
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * Find By Name
     * @param name name of the searching order
     * @return found tags
     */
    @Override
    public Optional<TagEntity> findByName(String name) {
        try {
            TagEntity tag = entityManager.createQuery(FIND_BY_NAME, TagEntity.class)
                    .setParameter("name", name).getSingleResult();
            return Optional.of(tag);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Get Most Widely User Tag Of User
     * @param userId -> id of the user
     * @return found tags
     */
    @Override
    public List<TagEntity> getMostWidelyUserTagOfUser(Long userId) {
        return entityManager.createNativeQuery(GET_MOST_USED_TAG_OF_USER, TagEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
