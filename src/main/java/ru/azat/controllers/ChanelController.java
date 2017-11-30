package ru.azat.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.azat.models.TLSession;
import ru.azat.repositories.TLSessionRepository;
import ru.azat.services.ApiService;
import ru.azat.services.ChanelService;
import ru.azat.services.ChanelServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
public class ChanelController {

    @Autowired
    TLSessionRepository tlSessionRepository;

    @Autowired
    ChanelService chanelService;

    @GetMapping(value = "/channels")
    public String getChanelPage(@ModelAttribute("model") ModelMap model){
        List<TLSession> sessions = tlSessionRepository.findAll();
        model.addAttribute("sessions", sessions);
        return "channel_page";
    }

    @PostMapping(value = "/channels")
    public String addChanel(@ModelAttribute("model") ModelMap model,
                            @RequestParam("id") Long userId,
                            @RequestParam("channelName") String chanelName){
        try {
            chanelService.addUserToChanel(userId, chanelName);
        } catch (IOException | TimeoutException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage() + " ");
            return "error_page";
        }
        return "channel_add";
    }



}
