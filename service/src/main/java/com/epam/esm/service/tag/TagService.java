package com.epam.esm.service.tag;

import com.epam.esm.dto.reponse.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService /*extends BaseService<TagPostRequest, TagGetResponse>*/ {
    TagGetResponse create(TagPostRequest p);

    TagGetResponse get(Long id);

    int delete(Long id);

    List<TagGetResponse> getAll(int limit, int offset);

    List<TagGetResponse> getMostWidelyUsedTagsOfUser(Long userId);
}
