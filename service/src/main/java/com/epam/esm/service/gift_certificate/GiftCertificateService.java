package com.epam.esm.service.gift_certificate;

import com.epam.esm.dto.reponse.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;

import java.util.List;

public interface GiftCertificateService /*extends BaseService<GiftCertificatePostRequest, GiftCertificateGetResponse>*/ {

    GiftCertificateGetResponse create(GiftCertificatePostRequest postRequest);

    GiftCertificateGetResponse get(Long id);

    int delete(Long id);

    List<GiftCertificateGetResponse> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int limit, int offset
    );

    GiftCertificateGetResponse update(GiftCertificateUpdateRequest update, Long certificateId);

    GiftCertificateGetResponse updateDuration(Integer duration, Long id);

    List<GiftCertificateGetResponse> searchWithMultipleTags(List<String> tags, int limit, int offset);
}
