package ru.azat.models;

import ru.azat.models.api.TelegramBot;
import ru.azat.models.api.engine.MyApiState;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UserChanelApi {
    public void addUserToChanel(String userPhone, String chanelName, TLSession session) throws IOException, TimeoutException, IllegalArgumentException {
        TelegramBot bot = new TelegramBot();
        if(session == null){
            throw new IllegalArgumentException("Такой телефон не найден");
        }
        try {
            MyApiState apiState = session.getApiState();
            bot.createApi(apiState);
            bot.addChanelByName(chanelName);
            bot.close();
        }catch (TimeoutException e){
            throw new TimeoutException(e.getMessage());
        }finally{
            bot.close();
        }
    }
}
