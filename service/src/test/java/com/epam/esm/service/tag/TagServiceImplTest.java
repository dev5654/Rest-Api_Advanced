package com.epam.esm.service.tag;

import com.epam.esm.dto.reponse.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.utils.TagServiceTestUtils.getTagEntities;
import static com.epam.esm.service.utils.TagServiceTestUtils.getTagGetResponses;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagEntity tag;
    private TagGetResponse tagGetResponse;
    private TagPostRequest tagPostRequest;

    @BeforeEach
    public void setUp(){
        tag = new TagEntity();
        tag.setName("test tag");

        tagGetResponse = new TagGetResponse();
        tagGetResponse.setName("test tag response");

        tagPostRequest = new TagPostRequest();
        tagPostRequest.setName("test tag post");
    }

    @Test
    void canCreateTag(){
        when(tagRepository.create(tag)).thenReturn(tag);
        when(modelMapper.map(tag, TagGetResponse.class)).thenReturn(tagGetResponse);
        when(modelMapper.map(tagPostRequest, TagEntity.class)).thenReturn(tag);

        TagGetResponse response = tagService.create(tagPostRequest);

        assertNotNull(response);
    }

    @Test
    void canGetTagById(){
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(modelMapper.map(tag, TagGetResponse.class)).thenReturn(tagGetResponse);

        TagGetResponse response = tagService.get(1L);

        assertEquals(tagGetResponse, response);
    }

    @Test
    void canGetAll(){
        List<TagEntity> tagEntities = getTagEntities();
        List<TagGetResponse> tagGetResponses = getTagGetResponses();
        when(tagRepository.getAll(5, 0)).thenReturn(tagEntities);
        when(modelMapper.map(tagEntities, new TypeToken<List<TagGetResponse>>(){}.getType()))
                .thenReturn(tagGetResponses);

        List<TagGetResponse> all = tagService.getAll(5, 0);

        assertEquals(5, all.size());
    }

    @Test
    void canDeleteById(){
        when(tagRepository.delete(1L)).thenReturn(1);

        int delete = tagService.delete(1L);

        assertEquals(1, delete);
    }

    @Test
    void canGetMostWidelyUsedTagsOfUser(){
        List<TagEntity> tagEntities = getTagEntities();
        when(tagRepository.getMostWidelyUserTagOfUser(1L)).thenReturn(tagEntities);
        when(modelMapper.map(tagEntities, new TypeToken<List<TagGetResponse>>() {}.getType()))
                .thenReturn(getTagGetResponses());
        List<TagGetResponse> mostWidelyUsedTagsOfUser = tagService.getMostWidelyUsedTagsOfUser(1L);
        assertEquals(5, mostWidelyUsedTagsOfUser.size());
    }


}