package br.com.santos.vinicius.nifflerapi.runnable;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;

import java.util.List;

public class UserSaveRunnable implements Runnable {

    private final List<UserEntity> entitiesToUpdate;

    private final UserRepository userRepository;

    public UserSaveRunnable(List<UserEntity> entitiesToUpdate, UserRepository userRepository) {
        this.entitiesToUpdate = entitiesToUpdate;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        synchronized (this) {
            userRepository.saveAll(entitiesToUpdate);
        }
    }
}
