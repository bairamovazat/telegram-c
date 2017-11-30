package ru.azat.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RegistrationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String phone;
    @NonNull
    private String phoneHash;
    @NonNull
    private Date date;
    @NonNull
    private Boolean phoneRegistered;
    @NonNull
    private String uuid;
}
