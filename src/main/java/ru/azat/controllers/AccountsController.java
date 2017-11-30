package ru.azat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.telegram.api.engine.RpcException;
import ru.azat.forms.AddAccountForm;
import ru.azat.forms.SignInForm;
import ru.azat.forms.SignUpForm;
import ru.azat.models.RegistrationData;
import ru.azat.models.TLSession;
import ru.azat.repositories.RegistrationDataRepository;
import ru.azat.repositories.TLSessionRepository;
import ru.azat.services.ApiService;
import ru.azat.services.ChanelService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
public class AccountsController {

    @Autowired
    RegistrationDataRepository registrationDataRepository;

    @Autowired
    ApiService apiService;


    @GetMapping(value = "/")
    public String mainPage(@ModelAttribute("model") ModelMap model){
        return "main_page";
    }
    @GetMapping(value = "/accounts")
    public String addAccount(@ModelAttribute("model") ModelMap model) {
        return "account_page";
    }

    @PostMapping(value = "/accounts")
    public String sendCode(@ModelAttribute("model") ModelMap model, @ModelAttribute AddAccountForm accountForm, RedirectAttributes redirectAttributes){
        RegistrationData registrationData;
        try {
            registrationData = apiService.sendCodeFromPhone(accountForm.getPhone());
        } catch (TimeoutException | RpcException e) {
            model.addAttribute("error", e.getMessage());
            //apiService.closeApi();
            return "error_page";
        }
        redirectAttributes.addAttribute("id" , registrationData.getUuid());
        registrationDataRepository.save(registrationData);
        if(registrationData.getPhoneRegistered()){
            return "redirect:/sign_in";
        }
        return "redirect:/sign_up";
    }

    @GetMapping(value = "/sign_up")
    public String signUp(@ModelAttribute("model") ModelMap model, @RequestParam("id") String uuid) {
        RegistrationData registrationData = registrationDataRepository.findOneByUuid(uuid);
        if(registrationData == null){
            model.addAttribute("error","id передан неверно");
            return "error_page";
        }
        model.addAttribute("id", uuid);
        model.addAttribute("phone", registrationData.getPhone());
        model.addAttribute("date", registrationData.getDate());
        return "sign_up_page";
    }

    @GetMapping(value = "/sign_in")
    public String signIn(@ModelAttribute("model") ModelMap model, @RequestParam("id") String uuid){
        RegistrationData registrationData = registrationDataRepository.findOneByUuid(uuid);
        if(registrationData == null){
            model.addAttribute("error","id передан неверно");
            return "error_page";
        }
        model.addAttribute("id", uuid);
        model.addAttribute("phone", registrationData.getPhone());
        model.addAttribute("date", registrationData.getDate());
        return "sign_in_page";
    }

    @PostMapping(value = "/sign_up")
    public String signUpAccount(@ModelAttribute("model") ModelMap model, @ModelAttribute SignUpForm signUpForm, @RequestParam("id") String uuid){
        try {
            apiService.signUpAndSaveState(signUpForm, uuid);
        } catch (TimeoutException | IOException e) {
            model.addAttribute("error", e.getMessage());
            return "error_page";
        }finally {
            //apiService.closeApi();
        }
        return "account_success";
    }

    @PostMapping(value = "/sign_in")
    public String signInAccount(@ModelAttribute("model") ModelMap model, @ModelAttribute SignInForm signInForm, @RequestParam("id") String uuid){
        try {
            apiService.signInAndSaveState(signInForm, uuid);
        } catch (TimeoutException | IOException e) {
            model.addAttribute("error",e.getMessage());
            return "error_page";
        }finally {
            //apiService.closeApi();
        }
        return "account_success";
    }

    @GetMapping(value = "/close_all_api")
    public String resetApi(@ModelAttribute("model") ModelMap model){
        apiService.closeAppBots();
        return "api_reset";
    }


    //http://localhost:8080/sign_up?smsCode=59722&firstName=Andrei&secondName=Berukov
}
