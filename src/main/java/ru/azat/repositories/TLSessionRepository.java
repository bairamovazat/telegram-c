package ru.azat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.azat.models.TLSession;

public interface TLSessionRepository extends JpaRepository<TLSession, Long> {
    TLSession findByPhone(String phone);
}
