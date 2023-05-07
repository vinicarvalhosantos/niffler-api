package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface UserMessageRepository extends CrudRepository<UserMessageEntity, String> {
}
