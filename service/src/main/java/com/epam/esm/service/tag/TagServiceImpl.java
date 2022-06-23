package com.epam.esm.service.tag;

import com.epam.esm.dto.reponse.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.repository.tag.TagRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public TagGetResponse create(TagPostRequest createTag) {
        TagEntity tagEntity = modelMapper.map(createTag, TagEntity.class);
        TagEntity createdTag = tagRepository.create(tagEntity);
        return modelMapper.map(createdTag, TagGetResponse.class);
    }

    @Override
    public TagGetResponse get(Long tagId) {
        Optional<TagEntity> tagOptional = tagRepository.findById(tagId);
        if(tagOptional.isPresent())
            return modelMapper.map(tagOptional.get(), TagGetResponse.class);
        throw new NoDataFoundException("no tag found with id: " + tagId);
    }

    @Override
    @Transactional
    public int delete(Long tagId) {
        return tagRepository.delete(tagId);
    }


    @Override
    public List<TagGetResponse> getAll(int limit, int offset) {
        List<TagEntity> allTags = tagRepository.getAll(limit, offset);
        return modelMapper.map(allTags, new TypeToken<List<TagGetResponse>>() {}.getType());
    }

    @Override
    public List<TagGetResponse> getMostWidelyUsedTagsOfUser(Long userId) {
        List<TagEntity> mostWidelyUserTagsOfUser = tagRepository.getMostWidelyUserTagOfUser(userId);
        if(mostWidelyUserTagsOfUser.isEmpty())
            throw new NoDataFoundException("this user haven't used any tags");
        return modelMapper.map(mostWidelyUserTagsOfUser, new TypeToken<List<TagGetResponse>>() {}.getType());
    }
}