package me.ger_tret.goblin_stock_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.constant.BrokerType;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;
import me.ger_tret.goblin_stock_exchange.exception.GseException;
import me.ger_tret.goblin_stock_exchange.exception.InsufficientGoldException;
import me.ger_tret.goblin_stock_exchange.mapper.EntityMapper;
import me.ger_tret.goblin_stock_exchange.repository.BrokerRepository;
import me.ger_tret.goblin_stock_exchange.service.BrokerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrokerServiceImpl implements BrokerService {
    private final BrokerRepository brokerRepository;
    private final EntityMapper mapper;
    private final static String BROKER_NOT_FOUND_EXCEPTION = "Broker not found";

    @Override
    public BrokerDto getBrokerDtoById(UUID id) {
        return brokerRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GseException(BROKER_NOT_FOUND_EXCEPTION));
    }

    @Override
    public Broker getBrokerById(UUID id) {
        return brokerRepository.findById(id)
                .orElseThrow(() -> new GseException(BROKER_NOT_FOUND_EXCEPTION));
    }


    @Override
    public BrokerDto getBrokerByUsername(String username) {
        return brokerRepository.findByUsername(username)
                .map(mapper::toDto)
                .orElseThrow(() -> new GseException(BROKER_NOT_FOUND_EXCEPTION));
    }

    @Override
    @Transactional
    public BrokerDto registerBroker(String username) {
        Broker broker = Broker.builder()
                .username(username)
                .brokerType(BrokerType.HUMAN)
                .goldBalance(new BigDecimal("1000.0000"))
                .reputation(0)
                .build();
        return mapper.toDto(brokerRepository.save(broker));
    }

    @Override
    public void validateBalance(UUID id, BigDecimal requiredAmount) {
        Broker broker = brokerRepository.findById(id)
                .orElseThrow(() -> new GseException(BROKER_NOT_FOUND_EXCEPTION));

        if (broker.getGoldBalance().compareTo(requiredAmount) < 0) {
            throw new InsufficientGoldException(id);
        }
    }

    @Override
    @Transactional
    public void updateBalance(UUID id, BigDecimal amount) {
        Broker broker = brokerRepository.findByIdWithLock(id)
                .orElseThrow(() -> new GseException(BROKER_NOT_FOUND_EXCEPTION));

        BigDecimal newBalance = broker.getGoldBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientGoldException(id);
        }
        broker.setGoldBalance(newBalance);
        brokerRepository.save(broker);
    }
}