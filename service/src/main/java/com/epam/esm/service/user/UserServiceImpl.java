package com.epam.esm.service.user;

import com.epam.esm.dto.reponse.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserGetResponse create(UserPostRequest userPostRequest) {
        checkExist(userPostRequest.getUsername());
        UserEntity userEntity = modelMapper.map(userPostRequest, UserEntity.class);
        UserEntity saved = userRepository.create(userEntity);
        return modelMapper.map(saved, UserGetResponse.class);
    }

    @Override
    public UserGetResponse get(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()) {
            return modelMapper.map(userEntity.get(), UserGetResponse.class);
        }
        throw new NoDataFoundException("no user found with id: " + id);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<UserGetResponse> getAll(int limit, int offset) {
        List<UserEntity> userEntities = userRepository.getAll(limit, offset);
        if(userEntities.size() == 0)
            throw new NoDataFoundException("no users found");
        return modelMapper.map(userEntities, new TypeToken<List<UserGetResponse>>() {
        }.getType());
    }

    void checkExist(String username){
         if (userRepository.findByName(username).isPresent())
            throw new DataAlreadyExistException("user with username: \"" + username + "\" already exists");
    }
}