package br.com.vinicius.santos.nifflerapi.singleton;

import java.util.ArrayList;
import java.util.List;

public class TwitchStream {

    private static TwitchRequestsRetrofit singleInstance = null;

    public static boolean streamIsOffline = true;

    public static boolean streamIsOnline = false;

    public static List<String> gamesList = new ArrayList<>();

    public static String lastGame = "";

}
