package com.epam.esm.repository.gift_certificate;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends CrudRepository<GiftCertificateEntity, Long>/*, GiftCertificateQueries */ {
    String GET_ALL = "select * from gift_certificate gc";
    String DELETE = "delete from GiftCertificateEntity where id = :id";
    String UPDATE_DURATION = "update gift_certificate set duration = :duration, last_update_date = :time where id = :id";
    String GET_ALL_WITH_SEARCH = "select * from gift_certificate gc" + " where gc.name ilike '%'|| :searchWord || '%' or gc.description ilike '%'|| :searchWord || '%'";
    String SEARCH_WITH_MULTIPLE_TAGS = "select gc FROM GiftCertificateEntity gc " + "join gc.tagEntities t " + "where t in (:tags) " + "group by gc.id " + "having count(gc) = :tagCount";
    String GET_ALL_WITH_SEARCH_AND_TAG_NAME = """                 
              select * from gift_certificate gc join certificate_tag ct on gc.id = ct.certificate_id
            where ct.tag_id = :tagId and
            (gc.name ilike '%'|| :searchWord || '%' or gc.description ilike '%'|| :searchWord || '%')""";

    String ORDER_NAME_DATE_DESC = " order by gc.name desc, gc.create_date desc";
    String ORDER_NAME_DATE = " order by gc.name, gc.create_date";
    String ORDER_NAME_DESC = " order by gc.name desc";
    String ORDER_NAME = " order by gc.name";
    String ORDER_DATE_DESC = " order by gc.create_date desc";
    String ORDER_DATE = " order by gc.create_date";
    String NO_ORDER = "";

    GiftCertificateEntity updateDuration(GiftCertificateEntity certificateEntity);

    List<GiftCertificateEntity> getAllOnly(boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset);

    List<GiftCertificateEntity> getAllWithSearchAndTagName(String searchWord, Long tagId, boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset);

    List<GiftCertificateEntity> getAllWithSearch(String searchWord, boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset);


    List<GiftCertificateEntity> searchWithMultipleTags(List<TagEntity> tags, int limit, int offset);

    default String getSorting(boolean doNameSort, boolean doDateSort, boolean isDescending) {
        if (doNameSort) {
            if (doDateSort) {
                if (isDescending) return ORDER_NAME_DATE_DESC;
                return ORDER_NAME_DATE;
            }
            if (isDescending) return ORDER_NAME_DESC;
            return ORDER_NAME;
        } else if (doDateSort) {
            if (isDescending) {
                return ORDER_DATE_DESC;
            }
            return ORDER_DATE;
        } else return NO_ORDER;
    }

    int delete(Long id);

    GiftCertificateEntity update(GiftCertificateEntity certificate);
}
