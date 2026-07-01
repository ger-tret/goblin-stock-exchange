package me.ger_tret.goblin_stock_exchange.repository;

import jakarta.persistence.LockModeType;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BrokerRepository extends JpaRepository<Broker, UUID> {
    Optional<Broker> findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Broker b where b.id = :id")
    Optional<Broker> findByIdWithLock(UUID id);

}
