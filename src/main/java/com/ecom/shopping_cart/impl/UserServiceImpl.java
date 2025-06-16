package com.ecom.shopping_cart.impl;

import com.ecom.shopping_cart.model.UserDetails;
import com.ecom.shopping_cart.repository.UserRepository;
import com.ecom.shopping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails saveUser(UserDetails user) {
        UserDetails savedUser;
        savedUser = userRepository.save(user);
        return savedUser;
    }
}
