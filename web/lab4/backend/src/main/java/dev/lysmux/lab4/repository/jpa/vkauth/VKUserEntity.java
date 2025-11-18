package dev.lysmux.lab4.repository.jpa.vkauth;

import dev.lysmux.lab4.repository.jpa.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vk_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VKUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "vk_id", nullable = false)
    private long vkId;

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
