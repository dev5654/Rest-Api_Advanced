package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.UnknownDataBaseException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepositoryImpl implements UserRepository{
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates a new instance of an entity in the database.
     * @param  userEntity -> Object to create
     * @return optional id of created object
     */
    @Override
    public UserEntity create(UserEntity userEntity) {
        entityManager.persist(userEntity);
        if(userEntity.getId() != null)
            return userEntity;
        throw new UnknownDataBaseException("there was a problem while creating gift certificate. Try again");
    }

    /**
     * Gets all existing entities with provided type and provided limit and offset.
     * @param limit limit of entities
     * @param offset offset for the entities
     * @return list of entities
     */
    @Override
    public List<UserEntity> getAll(int limit, int offset) {
        return entityManager.createQuery(GET_ALL, UserEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

    }

    /**
     * Gets entity with the provided id.
     * @param id id of the needed object
     * @return optional object of provided type
     */
    @Override
    public Optional<UserEntity> findById(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        return Optional.ofNullable(userEntity);
       /* if(userEntity != null)
            return Optional.of(userEntity);
        return Optional.empty();*/
    }

    /**
     * Gets entity with the provided name.
     * @param name name of the needed object
     * @return optional object of provided type
     */
    @Override
    public Optional<UserEntity> findByName(String name) {
        try {
            UserEntity userEntity = entityManager.createQuery(FIND_BY_NAME,
                            UserEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(userEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }



}
