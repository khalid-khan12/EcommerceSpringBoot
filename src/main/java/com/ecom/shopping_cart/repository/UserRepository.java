package com.ecom.shopping_cart.repository;

import com.ecom.shopping_cart.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {
}
