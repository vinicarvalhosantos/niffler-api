package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface LastUserMessageRepository extends CrudRepository<LastUserMessageEntity, String> {

    Optional<LastUserMessageEntity> findLastUserMessageByUserId(Long userId);

}
