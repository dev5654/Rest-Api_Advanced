package com.epam.esm.service.tag;

import com.epam.esm.dto.reponse.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService extends BaseService<TagPostRequest, TagGetResponse> {
    List<TagGetResponse> getAll(int limit, int offset);

    List<TagGetResponse> getMostWidelyUsedTagsOfUser(Long userId);
}
