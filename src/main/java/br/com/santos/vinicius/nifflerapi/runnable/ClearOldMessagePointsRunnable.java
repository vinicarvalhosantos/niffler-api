package br.com.santos.vinicius.nifflerapi.runnable;

import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserMessageRepository;

import java.util.List;

public class ClearOldMessagePointsRunnable implements Runnable {

    private final List<UserMessageEntity> entitiesToClear;

    private final UserMessageRepository userMessageRepository;

    public ClearOldMessagePointsRunnable(List<UserMessageEntity> entitiesToClear, UserMessageRepository userMessageRepository) {
        this.entitiesToClear = entitiesToClear;
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public void run() {
        synchronized (this) {
            userMessageRepository.deleteAll(entitiesToClear);
        }
    }
}
