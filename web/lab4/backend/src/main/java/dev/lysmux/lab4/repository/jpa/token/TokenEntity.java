package dev.lysmux.lab4.repository.jpa.token;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private boolean banned = false;

    @CurrentTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
