package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>, UserQueries{

    Optional<UserEntity> findByName(String name);
}
