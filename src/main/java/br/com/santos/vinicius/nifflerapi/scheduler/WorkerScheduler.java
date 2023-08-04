package br.com.santos.vinicius.nifflerapi.scheduler;

import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@EnableAsync
@Component
public class WorkerScheduler {

    @Autowired
    LastUserMessageService lastUserMessageService;

    @Autowired
    UserMessageService userMessageService;

    @Async
    @Scheduled(cron = "0 10 0 * * ?", zone = "GMT-3")
    public void clearOldInformationsTaskAsync() {
        lastUserMessageService.clearLastUserMessages();
        userMessageService.clearOldMessagePoints();
    }

}
