package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserMessageRepository extends JpaRepository<UserMessageEntity, Long> {

    @Query("select new UserMessageEntity(mp.id, mp.user, mp.messageLength, mp.pointsToAdd, mp.spam, mp.messageId, mp.deleted, mp.createdAt) from UserMessageEntity mp\n" +
            "where month(mp.createdAt) <> month(now())\n" +
            "and ((day(now()) between 1 and 15 and day(mp.createdAt) between 1 and 15)" +
            "or (day(now()) between 16 and 31 and day(mp.createdAt) between 16 and 31))")
    List<UserMessageEntity> findAllOldMessages();

    Optional<UserMessageEntity> findFirstByUserIdAndSpamIsFalseAndDeletedIsFalse(Long userId, Sort sort);

    Optional<UserMessageEntity> findByMessageId(String messageId);

    List<UserMessageEntity> findAllByUserIn(List<UserEntity> users);

}
