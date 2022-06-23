package com.epam.esm.service.base;

public interface BaseService<P, G> {
    G create(P p);

    G get(Long id);

    int delete(Long id);
}
