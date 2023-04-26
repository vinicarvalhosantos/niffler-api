package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.BlacklistEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface BlacklistRepository extends CrudRepository<BlacklistEntity, String> {


    Optional<BlacklistEntity> findByUserId(Long userId);

    Optional<BlacklistEntity> findByUsername(String username);

    void deleteByUsername(String username);

    void deleteByUserId(Long userId);

}
