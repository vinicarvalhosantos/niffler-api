package br.com.santos.vinicius.nifflerapi.scheduler;

import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import br.com.santos.vinicius.nifflerapi.singleton.UserCheers;
import io.jsonwebtoken.lang.Assert;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@EnableAsync
@Component
public class WorkerScheduler {

    final LastUserMessageService lastUserMessageService;

    final UserMessageService userMessageService;

    public WorkerScheduler(LastUserMessageService lastUserMessageService, UserMessageService userMessageService) {
        Assert.notNull(lastUserMessageService, "LastUserMessageService must not be null");
        Assert.notNull(userMessageService, "UserMessageService must not be null");
        this.lastUserMessageService = lastUserMessageService;
        this.userMessageService = userMessageService;
    }

    @Async
    @Scheduled(cron = "0 5 0 * * ?", zone = "GMT-3")
    public void clearOldInformationsTaskAsync() {
        lastUserMessageService.clearLastUserMessages();
        UserCheers.getInstance().clear();
        //userMessageService.clearOldMessagePoints();
    }

}
