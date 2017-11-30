package ru.azat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.azat.models.RegistrationData;

import java.util.UUID;

public interface RegistrationDataRepository extends JpaRepository<RegistrationData,Long>{
    RegistrationData findOneByUuid(String uuid);
}
