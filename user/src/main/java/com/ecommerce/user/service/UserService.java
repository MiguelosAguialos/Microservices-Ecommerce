package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).toList();
    }

    public Optional<UserResponse> fetchUserById(Long id){
       return userRepository.findById(id).map(this::mapToUserResponse);
    }

    public String createUser(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);
        user.setAddresses(userRequest.getAddresses().stream().map(this::mapToAddress).toList());
        user.getAddresses().forEach(address -> address.setUser(user));
        userRepository.save(user);
        return "User created successfully";
    }

    public Optional<Boolean> updateUser(Long id, UserRequest updatedUser){
        return userRepository.findById(id)
                .map(existingUser -> {
                    BeanUtils.copyProperties(updatedUser, existingUser, "id", "addresses");
                    existingUser.getAddresses().clear();
                    existingUser.getAddresses().addAll(updatedUser.getAddresses().stream().map(this::mapToAddress).toList());
                    existingUser.getAddresses().forEach(address -> address.setUser(existingUser));
                    userRepository.save(existingUser);
                    return true;
                });
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setAddresses(user.getAddresses().stream().map(this::mapToAddressDTO).toList());
        return userResponse;
    }

    private Address mapToAddress(AddressDTO addressDTO) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        return address;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(address, addressDTO);
        return addressDTO;
    }
}
