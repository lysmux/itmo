package dev.lysmux.lab4.repository.jpa.user;

import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class JPAUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public User addUser(User user) {
        UserEntity userEntity = toEntity(user);
        em.persist(userEntity);
        return fromEntity(userEntity);
    }

    @Override
    public User getUserById(String userId) {
        UserEntity user = em.find(UserEntity.class, UUID.fromString(userId));

        return fromEntity(user);
    }

    @Override
    public User getUserByName(String username) {
        List<UserEntity> users = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .getResultList();

        return users.isEmpty() ? null : fromEntity(users.getFirst());
    }

    @Override
    public boolean isUserExists(String username) {
        return em.createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.username = :username", Long.class)
                .setParameter("username",  username)
                .getSingleResult() > 0;
    }


    private User fromEntity(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId().toString())
                .username(entity.getUsername())
                .build();
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUsername(user.username());
        return entity;
    }
}
