package ru.azat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telegram.api.auth.TLSentCode;
import org.telegram.api.engine.RpcException;
import ru.azat.forms.SignInForm;
import ru.azat.forms.SignUpForm;
import ru.azat.models.RegistrationData;
import ru.azat.models.TLSession;
import ru.azat.models.api.TelegramBot;
import ru.azat.repositories.TLSessionRepository;
import ru.azat.repositories.RegistrationDataRepository;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
//@Scope(value = "session")
public class ApiServiceImpl implements ApiService {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    RegistrationDataRepository registrationDataRepository;

    @Autowired
    TLSessionRepository TLSessionRepository;
    private static HashMap<String, TelegramBot> botsMap = new HashMap<>();

    @Value("${spring.datasource.nullSessionPhone}")
    private String nullSessionPhone;


    @Override
    public synchronized RegistrationData sendCodeFromPhone(String phone) throws TimeoutException, RpcException {
        TelegramBot bot = new TelegramBot();
        bot.createApi();
        bot.initConfigs();
        System.out.println("ApiServiceImpl init");
        UUID uuid = UUID.randomUUID();
        botsMap.put(uuid.toString(), bot);
        TLSentCode sentCode = bot.sendCode(phone);
        return RegistrationData.builder()
                .phone(phone)
                .phoneHash(sentCode.getPhoneCodeHash())
                .date(new Date())
                .phoneRegistered(sentCode.isPhoneRegistered())
                .uuid(uuid.toString())
                .build();
    }

    @Override
    public synchronized void signUp(SignUpForm signUpForm, String uuid) throws TimeoutException, IOException {
        TelegramBot bot = botsMap.get(uuid);
        if (bot == null) {
            throw new IOException("bot is null");
        }
        RegistrationData registrationData = registrationDataRepository.findOneByUuid(uuid);
        bot.signUp(signUpForm.getSmsCode(), registrationData.getPhoneHash(), registrationData.getPhone(), signUpForm.getFirstName(), signUpForm.getSecondName());
        bot.close();
    }

    @Override
    public synchronized void signIn(SignInForm signInForm, String uuid) throws TimeoutException, IOException {
        TelegramBot bot = botsMap.get(uuid);
        if (bot == null) {
            throw new IOException("bot is null");
        }
        RegistrationData registrationData = registrationDataRepository.findOneByUuid(uuid);
        bot.signIn(signInForm.getSmsCode(), registrationData.getPhoneHash(), registrationData.getPhone());
        bot.close();
    }

    @Override
    public synchronized void signUpAndSaveState(SignUpForm signUpForm, String uuid) throws TimeoutException, IOException {
        TelegramBot bot = botsMap.get(uuid);
        if (bot == null) {
            throw new IOException("bot is null");
        }
        RegistrationData registrationData = registrationDataRepository.findOneByUuid(uuid);
        bot.signUp(signUpForm.getSmsCode(), registrationData.getPhoneHash(), registrationData.getPhone(), signUpForm.getFirstName(), signUpForm.getSecondName());
        TLSession tlSession = TLSession.builder()
                .apiState(bot.getApiState())
                .firstName(signUpForm.getFirstName())
                .secondName(signUpForm.getSecondName())
                .phone(registrationData.getPhone())
                .date(new Date())
                .build();
        TLSessionRepository.save(tlSession);
        bot.close();
    }

    @Override
    public synchronized void signInAndSaveState(SignInForm signInForm, String uuid) throws TimeoutException, IOException {
        TelegramBot bot = botsMap.get(uuid);
        if (bot == null) {
            throw new IOException("bot is null");
        }
        RegistrationData registrationData = registrationDataRepository.findOneByUuid(uuid);
        bot.signIn(signInForm.getSmsCode(), registrationData.getPhoneHash(), registrationData.getPhone());
        TLSession tlSession = TLSession.builder()
                .apiState(bot.getApiState())
                .firstName("registered account")
                .secondName("registered account")
                .phone(registrationData.getPhone())
                .date(new Date())
                .build();
        TLSessionRepository.save(tlSession);
        bot.close();
    }

    @Override
    public synchronized void closeApi(String uuid) {
        botsMap.get(uuid).close();
    }

    @Override
    public synchronized void closeAppBots(){
        for (String key : botsMap.keySet()) {
            botsMap.get(key).close();
        }
    }

    @Override
    public void createAndSaveNullApiState(String phone) throws TimeoutException, RpcException {
        TelegramBot bot = new TelegramBot();
        bot.createApi();
        bot.initConfigs();
        TLSession nullSession = TLSession
                .builder()
                .apiState(bot.getApiState())
                .date(new Date())
                .firstName("Пустая сессия")
                .secondName("Пустая сессия")
                .phone("00000000000")
                .build();
        TLSessionRepository.save(nullSession);
        bot.close();
    }
}
