package br.com.santos.vinicius.nifflerapi.singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCheers {

    private static UserCheers singleInstance = null;

    public final Map<Long, List<Integer>> cheersSent = new HashMap<>();

    private UserCheers() {
    }

    public static UserCheers getInstance() {
        if (singleInstance == null) {
            singleInstance = new UserCheers();
        }

        return singleInstance;
    }

    public void addCheersIntoList(Long userId, int amount) {
        if (cheersSent.isEmpty() || cheersSent.get(userId) == null) {
            cheersSent.put(userId, new ArrayList<>());
        }

        cheersSent.get(userId).add(amount);
    }

    public int cheersAmountSentByUser(Long userId) {
        if (!cheersSent.isEmpty() && cheersSent.get(userId) != null && !cheersSent.get(userId).isEmpty()) {
            return cheersSent.get(userId).stream().reduce(0, Integer::sum);
        }

        return 0;
    }

    public void clear() {
        this.cheersSent.clear();
    }

    public void clear(Long userId) {
        if (this.cheersSent.get(userId) != null)
            this.cheersSent.get(userId).clear();
    }


}
