package ru.azat.services;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.azat.models.TLSession;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public interface ChanelService {
    void addUserToChanel(String userPhone, String chanelInvite) throws IOException, TimeoutException;

    void addUserToChanel(Long userId, String chanelName) throws IOException, TimeoutException, IllegalArgumentException;

    void addUserToChanel(TLSession session, String chanelName) throws IOException, TimeoutException;
}
