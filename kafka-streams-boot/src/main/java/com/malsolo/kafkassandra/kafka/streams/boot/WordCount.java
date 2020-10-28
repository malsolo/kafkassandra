package com.malsolo.kafkassandra.kafka.streams.boot;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordCount {
    private String word;
    private long count;
    private LocalDateTime start;
    private LocalDateTime end;
}
