package com.envelo.currencyexchange.model.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SystemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeStamp;
    private String source;
    private String description;
}
