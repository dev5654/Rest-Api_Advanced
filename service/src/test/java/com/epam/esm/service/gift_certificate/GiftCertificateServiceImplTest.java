package com.epam.esm.service.gift_certificate;

import com.epam.esm.dto.reponse.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.gift_certificate.GiftCertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.utils.GiftCertificateServiceTestUtils.*;
import static com.epam.esm.service.utils.TagServiceTestUtils.getTestTagEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private GiftCertificateEntity entity;
    private GiftCertificateGetResponse getResponse;
    private GiftCertificatePostRequest postRequest;

    @BeforeEach
    void setUp() {
        entity = getGiftCertificateEntity();
        getResponse = getGiftCertificateGetResponse();
        postRequest = getGiftCertificatePostRequest();
    }

    @Test
    void canCreateCertificate() {
        when(giftCertificateRepository.create(entity)).thenReturn(entity);
        when(modelMapper.map(postRequest, GiftCertificateEntity.class)).thenReturn(entity);
        when(modelMapper.map(entity, GiftCertificateGetResponse.class)).thenReturn(getResponse);
        GiftCertificateGetResponse response = giftCertificateService.create(postRequest);

        assertEquals("Store", response.getName());
    }

    @Test
    void canGetById() {
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, GiftCertificateGetResponse.class)).thenReturn(getResponse);
        GiftCertificateGetResponse response = giftCertificateService.get(1L);

        assertEquals("Store", response.getName());
    }

    @Test
    void canDeleteById() {
        when(giftCertificateRepository.delete(1L)).thenReturn(1);
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(entity));
        int delete = giftCertificateService.delete(1L);

        assertEquals(1, delete);
    }

    @Test
    void canGetAll() {
        List<GiftCertificateEntity> giftCertificateEntities = getGiftCertificateEntities();
        List<GiftCertificateGetResponse> giftCertificateGetResponses = getGiftCertificateGetResponses();
        TagEntity tagEntity = getTestTagEntity();

        when(tagRepository.findByName("toy")).thenReturn(Optional.of(tagEntity));
        when(giftCertificateRepository.getAllOnly(false, false, false, 3, 0))
                .thenReturn(giftCertificateEntities);
        when(giftCertificateRepository
                .getAllWithSearch("Store", true, true, true, 1, 0))
                .thenReturn(giftCertificateEntities.subList(0, 1));
        when(giftCertificateRepository
                .getAllWithSearchAndTagName(
                        "Kid", 1L, false, false, false, 1, 0))
                .thenReturn(giftCertificateEntities.subList(1, 2));

        when(modelMapper.map(giftCertificateEntities, new TypeToken<List<GiftCertificateGetResponse>>(){}.getType()))
                .thenReturn(giftCertificateGetResponses);
        when(modelMapper.map(giftCertificateEntities.subList(0, 1), new TypeToken<List<GiftCertificateGetResponse>>(){}.getType()))
                .thenReturn(giftCertificateGetResponses.subList(0, 1));
        when(modelMapper.map(giftCertificateEntities.subList(1, 2), new TypeToken<List<GiftCertificateGetResponse>>(){}.getType()))
                .thenReturn(giftCertificateGetResponses.subList(1, 2));

        List<GiftCertificateGetResponse> allWithoutSearch = giftCertificateService.
                getAll("", null, false, false, false, 3, 0);
        List<GiftCertificateGetResponse> allWithSearch = giftCertificateService
                .getAll("Store", null, true, true, true, 1, 0);
        List<GiftCertificateGetResponse> allWithTagAndSearch = giftCertificateService.getAll(
                "Kid", "toy", false, false, false, 1, 0);


        assertEquals(3, allWithoutSearch.size());
        assertNotNull(allWithSearch);
        assertNotNull(allWithTagAndSearch);
    }

    @Test
    void canUpdateCertificate() {
        GiftCertificateUpdateRequest update = getGiftCertificateUpdateRequest();
        GiftCertificateEntity old = getGiftCertificateEntity();

        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(old));
        when(giftCertificateRepository.update(old)).thenReturn(entity);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(entity, GiftCertificateGetResponse.class)).thenReturn(getResponse);
        doNothing().when(modelMapper).map(update, old);

        GiftCertificateGetResponse update1 = giftCertificateService.update(update, 1L);

        assertEquals("Store", update1.getName());
    }

    @Test
    void updateDuration() {
        when(giftCertificateRepository.updateDuration(entity)).thenReturn(entity);
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, GiftCertificateGetResponse.class)).thenReturn(getResponse);

        GiftCertificateGetResponse response = giftCertificateService.updateDuration(1, 1L);
        assertEquals(10, response.getDuration());
    }

    @Test
    void searchWithMultipleTags() {
        List<String> tags = List.of("tag");
        List<TagEntity> tagEntities = List.of(getTestTagEntity());
        List<GiftCertificateEntity> giftCertificateEntities = getGiftCertificateEntities();
        List<GiftCertificateGetResponse> giftCertificateGetResponses = getGiftCertificateGetResponses();

        when(tagRepository.findByName("tag")).thenReturn(Optional.of(tagEntities.get(0)));
        when(giftCertificateRepository.searchWithMultipleTags(tagEntities, 1, 0))
                .thenReturn(giftCertificateEntities);
        when(modelMapper.map(giftCertificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {}.getType()))
                .thenReturn(giftCertificateGetResponses);

        List<GiftCertificateGetResponse> giftCertificateGetResponses1
                = giftCertificateService.searchWithMultipleTags(tags, 1, 0);
        assertEquals(3, giftCertificateGetResponses.size());
    }
}