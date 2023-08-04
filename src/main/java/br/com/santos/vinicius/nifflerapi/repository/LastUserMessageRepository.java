package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LastUserMessageRepository extends JpaRepository<LastUserMessageEntity, Long> {

    Optional<LastUserMessageEntity> findLastUserMessageByUserId(Long userId);

}
