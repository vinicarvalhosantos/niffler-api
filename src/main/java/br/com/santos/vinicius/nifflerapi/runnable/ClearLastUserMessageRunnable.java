package br.com.santos.vinicius.nifflerapi.runnable;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.LastUserMessageRepository;

import java.util.List;

public class ClearLastUserMessageRunnable implements Runnable {
    private final List<LastUserMessageEntity> entitiesToClear;

    private final LastUserMessageRepository lastUserMessageRepository;

    public ClearLastUserMessageRunnable(List<LastUserMessageEntity> entitiesToClear, LastUserMessageRepository lastUserMessageRepository) {
        this.entitiesToClear = entitiesToClear;
        this.lastUserMessageRepository = lastUserMessageRepository;
    }

    @Override
    public void run() {
        synchronized (this) {
            lastUserMessageRepository.saveAll(entitiesToClear);
        }
    }
}
