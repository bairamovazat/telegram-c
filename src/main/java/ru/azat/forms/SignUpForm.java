package ru.azat.forms;

import lombok.Data;

import java.util.UUID;

@Data
public class SignUpForm {
    private String firstName;
    private String secondName;
    private String smsCode;
}
