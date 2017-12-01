package ru.azat.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.azat.models.TLSession;
import ru.azat.models.api.TelegramBot;
import ru.azat.models.api.engine.MyApiState;
import ru.azat.repositories.TLSessionRepository;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class ChanelServiceImpl implements ChanelService {

    @Autowired
    TLSessionRepository tlSessionRepository;

    @Override
    public void addUserToChanel(String userPhone, String chanelName) throws IOException, TimeoutException, IllegalArgumentException, IOException, TimeoutException {
        TLSession session = tlSessionRepository.findByPhone(userPhone);
        addUserToChanel(session, chanelName);
    }


    @Override
    public void addUserToChanel(Long userId, String chanelName) throws IOException, TimeoutException, IllegalArgumentException {
        TLSession session = tlSessionRepository.findOne(userId);
        addUserToChanel(session, chanelName);
    }

    @Override
    public void addUserToChanel(TLSession session, String chanelName) throws IOException, TimeoutException {
        if (session == null) {
            throw new IllegalArgumentException("Такой телефон не найден");
        }
        TelegramBot bot = new TelegramBot();
        MyApiState apiState = session.getApiState();
        bot.createApi(apiState);
        bot.initConfigs();
        try {
            bot.addChanelByName(chanelName);
        } finally {
            bot.close();
        }
    }
}
