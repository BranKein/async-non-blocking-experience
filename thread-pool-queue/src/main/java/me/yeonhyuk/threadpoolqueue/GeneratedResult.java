package me.yeonhyuk.threadpoolqueue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "generated_result")
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedResult {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "delay", nullable = false)
    private Long delay;

    @Column(name = "new_id", nullable = false)
    private Long newId;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
}