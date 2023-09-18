package br.com.santos.vinicius.nifflerapi.repository;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Long userId);

    List<UserEntity> findAllByUsernameIn(List<String> usernames);

    @Query("select new UserEntity(u.id, u.username, u.displayName, u.createdAt, u.pointsToAdd, u.pointsAdded, u.isDeleted) from UserEntity u " +
            "where u.isDeleted = false")
    Page<UserEntity> findAllByDeletedIsFalse(Pageable pageable);
}
