package com.epam.esm.service.gift_certificate;

import com.epam.esm.dto.reponse.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.BreakingDataRelationshipException;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.exception.tag.InvalidTagException;
import com.epam.esm.repository.gift_certificate.GiftCertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * class -> GiftCertificateServiceImpl
 */
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService{
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    /**
     *
     * @param createCertificate
     * @return GiftCertificateGetResponse(Gift Certificate)
     */

    @Transactional
    @Override
    public GiftCertificateGetResponse create(GiftCertificatePostRequest createCertificate) {
        List<TagEntity> tagEntities = createCertificate.getTagEntities();
        GiftCertificateEntity certificateEntity = modelMapper.map(createCertificate, GiftCertificateEntity.class);
        if(createCertificate.getTagEntities() != null &&  !certificateEntity.getTagEntities().isEmpty())
            certificateEntity.setTagEntities(createTags(tagEntities));
        GiftCertificateEntity saved = giftCertificateRepository.create(certificateEntity);
        return modelMapper.map(saved, GiftCertificateGetResponse.class);
    }

    /**
     *
     * @param certificateId
     * @return GiftCertificateGetResponse(Gift Certificate)
     */
    @Override
    public GiftCertificateGetResponse get(Long certificateId) {
        GiftCertificateEntity certificate = giftCertificateRepository.findById(certificateId).get();
        return modelMapper.map(certificate, GiftCertificateGetResponse.class);
    }

    /**
     *
     * @param certificateId
     * @return id of deleted Gift Certificate
     */

    @Transactional
    @Override
    public int delete(Long certificateId) {
        if(checkExist(certificateId)) {
            try {
                return giftCertificateRepository.delete(certificateId);
            } catch (Exception e) {
                throw new BreakingDataRelationshipException("this certificate is ordered by users, so it cannot be deleted");
            }
        }
        throw new NoDataFoundException("no certificate to delete with id: " + certificateId);
    }

    /**
     *
     * @param searchWord -> The word which can be searched each field of Gift certificate
     * @param tagName -> The name of the tag which belongs to the current Gift certificate
     * @param doNameSort -> Boolean value which can give access to order  Gift certificate by their name
     * @param doDateSort -> Boolean value which can give access to order  Gift certificate by their date
     * @param isDescending -> Boolean value which can give access to order  Gift certificate by their name
     * @param limit -> Numeric value which is limit of pagination
     * @param offset -> Numeric value offset of Gift certificate list
     * @return Requested Gift Certificate
     */
    @Override
    public List<GiftCertificateGetResponse> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int limit, int offset)
    {
        List<GiftCertificateEntity> certificateEntities;
        if(tagName != null) {
            try {
                Long tagId = tagRepository.findByName(tagName).get().getId();
                certificateEntities = giftCertificateRepository.getAllWithSearchAndTagName(
                        searchWord, tagId, doNameSort, doDateSort, isDescending, limit, offset);
            }catch (NullPointerException e){
                throw new NoDataFoundException("there is no gift certificate with tag name: "  + tagName);
            }
        } else if(searchWord.isEmpty()){
            certificateEntities = giftCertificateRepository.getAllOnly(
                    doNameSort, doDateSort, isDescending, limit, offset);
        } else
            certificateEntities = giftCertificateRepository.getAllWithSearch(
                    searchWord, doNameSort, doDateSort, isDescending, limit, offset);
        if(certificateEntities.size() == 0)
            throw new NoDataFoundException("no matching gift certificate found");
        return modelMapper.map(certificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {}.getType());
    }

    /**
     *
     * @param -> update gift certificate DTO
     * @param -> certificateId -> numeric value id of Gift certificate
     * @return ->  Gift Certificate with updated values
     */
    @Override
    @Transactional
    public GiftCertificateGetResponse update(GiftCertificateUpdateRequest update, Long certificateId)
    {
        Optional<GiftCertificateEntity> old = giftCertificateRepository.findById(certificateId);
        if(old.isEmpty())
            throw new NoDataFoundException("certificate with id: " + certificateId + " not found");
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        GiftCertificateEntity certificate = old.get();
        List<TagEntity> tagEntities;
        if(certificate.getTagEntities() == null)
            tagEntities = new ArrayList<>();
        else
            tagEntities= certificate.getTagEntities();
        modelMapper.map(update, certificate);
        if(update.getTagEntities() != null && !update.getTagEntities().isEmpty()) {
            tagEntities.addAll(createTags(update.getTagEntities()));
        }
        certificate.setTagEntities(tagEntities);
        GiftCertificateEntity updated = giftCertificateRepository.update(certificate);
        return modelMapper.map(updated, GiftCertificateGetResponse.class);
    }

    /**
     *
     * @param duration -> numeric value which is duration of current Gift certificate
     * @param id -> numeric value which is id of the Gift certificate
     * @return -> duration updated Gift Certificate
     */
    @Override
    @Transactional
    public GiftCertificateGetResponse updateDuration(Integer duration, Long id) {
        if(duration <= 0)
            throw new InvalidCertificateException("certificate duration must be positive");
        Optional<GiftCertificateEntity> certificateEntityOptional = giftCertificateRepository.findById(id);
        if(certificateEntityOptional.isPresent()) {
            GiftCertificateEntity certificate = certificateEntityOptional.get();
            certificate.setDuration(duration);
            return modelMapper.map(giftCertificateRepository.updateDuration(certificate), GiftCertificateGetResponse.class);
        }
        throw new NoDataFoundException("cannot find gift certificate with id: " + id);
    }

    /**
     *
     * @param tags -> list of tag names
     * @param limit -> limit of the pagination
     * @param offset -> offset of the pagination
     * @return Filtered Gift Certificate list
     */
    @Override
    public List<GiftCertificateGetResponse> searchWithMultipleTags(
            List<String> tags, int limit, int offset)
    {
        List<TagEntity> tagEntities = new ArrayList<>();
        tags.forEach(tag -> {
            Optional<TagEntity> tagByName = tagRepository.findByName(tag);
            tagByName.ifPresent(tagEntities::add);
        });
        List<GiftCertificateEntity> certificateEntities
                = giftCertificateRepository.searchWithMultipleTags(tagEntities, limit, offset);
        if(certificateEntities.isEmpty())
            throw new NoDataFoundException("no certificate found with these tags");
        return modelMapper.map(certificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {}.getType());
    }

    /**
     *
     * @param tagEntities -> List of the tags which are created into the current gift certificate
     * @return created tag list
     */
    private List<TagEntity> createTags(List<TagEntity> tagEntities) {
        List<TagEntity> tagEntityList = new ArrayList<>();
        tagEntities.forEach(tag -> {
            if(tag.getName() == null || tag.getName().isEmpty()) {
                throw new InvalidTagException("tag name cannot be empty or null");
            }
            Optional<TagEntity> tagByName = tagRepository.findByName(tag.getName());
            if(tagByName.isPresent()) {
                tagEntityList.add(tagByName.get());
            }else{
                tagEntityList.add(tagRepository.create(tag));
            }
        });
        return tagEntityList;
    }

    /**
     *
     * @param id -> Id of the searched gift certificate
     * @return boolean -> find or not
     */
    private boolean checkExist(Long id) {
        return giftCertificateRepository.findById(id).isPresent();
    }
}
