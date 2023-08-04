package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {


    Optional<BlacklistEntity> findByUserId(Long userId);

    Optional<BlacklistEntity> findByUsername(String username);

    void deleteByUsername(String username);

    void deleteByUserId(Long userId);

}
