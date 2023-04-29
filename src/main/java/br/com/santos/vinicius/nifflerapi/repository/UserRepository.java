package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUserId(Long userId);
}
