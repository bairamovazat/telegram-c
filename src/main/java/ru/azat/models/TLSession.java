package ru.azat.models;

import lombok.*;
import ru.azat.models.api.engine.MyApiState;

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
public class TLSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String phone;
    private String firstName;
    private String secondName;
    private MyApiState apiState;
    private Date date;
}
