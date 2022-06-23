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

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService{
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GiftCertificateGetResponse create(GiftCertificatePostRequest createCertificate) {
        List<TagEntity> tagEntities = createCertificate.getTagEntities();
        GiftCertificateEntity certificateEntity = modelMapper.map(createCertificate, GiftCertificateEntity.class);
        if(createCertificate.getTagEntities() != null && certificateEntity.getTagEntities().size() != 0)
            certificateEntity.setTagEntities(createTags(tagEntities));
        GiftCertificateEntity saved = giftCertificateRepository.create(certificateEntity);
        return modelMapper.map(saved, GiftCertificateGetResponse.class);
    }

    @Override
    public GiftCertificateGetResponse get(Long certificateId) {
        GiftCertificateEntity certificate = giftCertificateRepository.findById(certificateId).get();
        return modelMapper.map(certificate, GiftCertificateGetResponse.class);
    }

    @Override
    @Transactional
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

    private boolean checkExist(Long id) {
        return giftCertificateRepository.findById(id).isPresent();
    }
}
