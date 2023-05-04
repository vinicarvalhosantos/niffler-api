package br.com.santos.vinicius.nifflerapi.runnable;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;

import java.util.List;

public class UserDeleteRunnable implements Runnable {

    private final List<UserEntity> entitiesToDelete;

    private final UserRepository userRepository;

    public UserDeleteRunnable(List<UserEntity> entitiesToDelete, UserRepository userRepository) {
        this.entitiesToDelete = entitiesToDelete;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        synchronized (this) {
            userRepository.deleteAll(entitiesToDelete);
        }
    }
}
