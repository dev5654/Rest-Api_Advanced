package com.epam.esm.repository.gift_certificate;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.UnknownDataBaseException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates a new instance of an entity in the database.
     * @param  certificate -> Object to create
     * @return optional id of created object
     */
    @Override
    public GiftCertificateEntity create(GiftCertificateEntity certificate) {
        entityManager.persist(certificate);
        if (certificate.getId() != null)
            return certificate;
        throw new UnknownDataBaseException("there was a problem while creating gift certificate. Try again");
    }

    /**
     * Gets all existing entities with provided type and provided limit and offset.
     * @param limit limit of entities
     * @param offset offset for the entities
     * @return list of entities
     */
    @Override
    public List<GiftCertificateEntity> getAll(int limit, int offset) {
        return entityManager
                .createQuery(GET_ALL, GiftCertificateEntity.class)
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
    public Optional<GiftCertificateEntity> findById(Long id) {
        GiftCertificateEntity certificateEntity = entityManager.find(GiftCertificateEntity.class, id);
        if (certificateEntity != null)
            return Optional.of(certificateEntity);
        throw new NoDataFoundException("no certificate found with id: " + id);
    }

    /**
     * Updates provided entity object.
     * @param certificateUpdate -> entity with fields that needed to be updated
     * @return the number of rows affected
     */
    public GiftCertificateEntity update(GiftCertificateEntity certificateUpdate) {
        return entityManager.merge(certificateUpdate);
    }
    /**
     * Deletes entity with the provided id.
     * @param id id of the object to be deleted
     * @return the number of rows affected
     */
    public int delete(Long id) {
        return entityManager
                .createQuery(DELETE)
                .setParameter("id", id)
                .executeUpdate();
    }
    /**
     * Update certificates duration
     * @param certificate -> updating certificate
     * @return updated certificate
     */
    @Override
    public GiftCertificateEntity updateDuration(GiftCertificateEntity certificate) {
        return entityManager.merge(
                        certificate);
    }

    /**
     * Search with multiple tags
     * @param tags id of the object to be deleted
     * @param limit limit to certificate
     * @param offset offset for certificate
     * @return list of found certificates
     */
    @Override
    public List<GiftCertificateEntity> searchWithMultipleTags(List<TagEntity> tags, int limit, int offset) {
        return entityManager.createQuery(SEARCH_WITH_MULTIPLE_TAGS, GiftCertificateEntity.class)
                .setParameter("tags", tags)
                .setParameter("tagCount", (long) tags.size())
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    /**
     * Get all with search and tag name
     * @param doDateSort boolean for sorting by date
     * @param doNameSort boolean for sorting by name
     * @param isDescending boolean for sorting by descending order
     * @return list of found certificates
     */
    @Override
    public List<GiftCertificateEntity> getAllWithSearchAndTagName(
            String searchWord, Long tagId, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int limit, int offset) {
        String query
                = GET_ALL_WITH_SEARCH_AND_TAG_NAME + getSorting(doNameSort, doDateSort, isDescending);
        return entityManager.createNativeQuery(
                        query, GiftCertificateEntity.class)
                .setParameter("searchWord", searchWord)
                .setParameter("tagId", tagId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Get all with search
     * @param searchWord the word searching from each field of the certificate
     * @param doDateSort boolean for sorting by date
     * @param doNameSort boolean for sorting by name
     * @param isDescending boolean for sorting by descending order
     * @param limit limit of certificate
     * @param offset offset for certificate
     * @return list of found certificates
     */
    @Override
    public List<GiftCertificateEntity> getAllWithSearch(
            String searchWord, boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset) {
        String query = GET_ALL_WITH_SEARCH + getSorting(doNameSort, doDateSort, isDescending);
        return entityManager.createNativeQuery(
                        query, GiftCertificateEntity.class)
                .setParameter("searchWord", searchWord)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    /**
     * Get all only
     * @param doDateSort boolean for sorting by date
     * @param doNameSort boolean for sorting by name
     * @param isDescending boolean for sorting by descending order
     * @param limit limit of certificate
     * @param offset offset for certificate
     * @return list of found certificates
     */
    @Override
    public List<GiftCertificateEntity> getAllOnly(
            boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset) {
        return entityManager
                .createNativeQuery(
                        GET_ALL + getSorting(doNameSort, doDateSort, isDescending),
                        GiftCertificateEntity.class)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }
}
