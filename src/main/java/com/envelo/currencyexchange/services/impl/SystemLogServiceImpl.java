package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.model.entities.SystemLog;
import com.envelo.currencyexchange.repositories.SystemLogRepository;
import com.envelo.currencyexchange.services.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository systemLogRepository;

    @Override
    public void saveLog(String source, String description) {
        SystemLog systemLog = SystemLog.builder()
                .source(source)
                .description(description)
                .timeStamp(LocalDateTime.now())
                .build();

        systemLogRepository.save(systemLog);
    }
}
