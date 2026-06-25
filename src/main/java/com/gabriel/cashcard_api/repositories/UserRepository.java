package com.gabriel.cashcard_api.repositories;

import com.gabriel.cashcard_api.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserModel, UUID> {
    Optional<UserModel> findByEmail(String email);
}
