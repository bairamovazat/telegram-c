package ru.azat.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.api.engine.RpcException;
import ru.azat.forms.SignInForm;
import ru.azat.forms.SignUpForm;
import ru.azat.models.RegistrationData;
import ru.azat.models.TLSession;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
//@Scope(value = "session")
public interface ApiService {
    RegistrationData sendCodeFromPhone(String phone) throws TimeoutException, RpcException;

    void signUp(SignUpForm signUpForm, String uuid) throws TimeoutException, IOException;

    void signIn(SignInForm signInForm, String uuid) throws TimeoutException, IOException;

    void signUpAndSaveState(SignUpForm signUpForm, String uuid) throws TimeoutException, IOException;

    void signInAndSaveState(SignInForm signInForm, String uuid) throws TimeoutException, IOException;

    void closeApi(String uuid);

    void closeAppBots();

    void createAndSaveNullApiState(String phone) throws TimeoutException, RpcException;
}
