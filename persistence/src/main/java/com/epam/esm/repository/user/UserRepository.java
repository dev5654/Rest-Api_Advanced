package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>/*, UserQueries*/{
    String GET_ALL = "select u from UserEntity u";
    String FIND_BY_NAME = "select u from UserEntity u where u.username = :name";

    Optional<UserEntity> findByName(String name);
}
