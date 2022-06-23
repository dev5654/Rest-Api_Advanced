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

    @Override
    public TagEntity create(TagEntity tagEntity) {
        entityManager.persist(tagEntity);
        if (tagEntity.getId() != null)
            return tagEntity;
        throw new UnknownDataBaseException("there was a problem while creating gift certificate. Try again");
    }

    @Override
    public List<TagEntity> getAll(int limit, int offset) {
        return entityManager
                .createQuery(GET_ALL, TagEntity.class)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Optional<TagEntity> findById(Long id) {
        TagEntity tagEntity = entityManager.find(TagEntity.class, id);
        if (tagEntity != null)
            return Optional.of(tagEntity);
        return Optional.empty();
    }

    @Override
    public TagEntity update(TagEntity obj) {
        return null;
    }

    @Override
    public int delete(Long id) {
        return entityManager
                .createQuery(DELETE_BY_ID)
                .setParameter("id", id)
                .executeUpdate();
    }

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

    @Override
    public List<TagEntity> getMostWidelyUserTagOfUser(Long userId) {
        return entityManager.createNativeQuery(GET_MOST_USED_TAG_OF_USER, TagEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
