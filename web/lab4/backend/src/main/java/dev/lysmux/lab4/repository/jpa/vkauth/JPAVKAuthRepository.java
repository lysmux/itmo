package dev.lysmux.lab4.repository.jpa.vkauth;

import dev.lysmux.lab4.auth.providers.vk.model.VKUser;
import dev.lysmux.lab4.auth.providers.vk.repository.VKAuthRepository;
import dev.lysmux.lab4.repository.jpa.user.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class JPAVKAuthRepository implements VKAuthRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public VKUser addUser(VKUser user) {
        VKUserEntity entity = toEntity(user);
        em.persist(entity);
        return fromEntity(entity);
    }

    @Override
    public VKUser getUser(long vkId) {
        System.out.println(Hibernate.class.getPackage().getImplementationVersion());

        List<VKUserEntity> entity = em.createQuery("SELECT u FROM VKUserEntity u WHERE u.vkId = :id", VKUserEntity.class)
                .setParameter("id", vkId)
                .getResultList();

        return entity.isEmpty() ? null : fromEntity(entity.getFirst());
    }

    private VKUser fromEntity(VKUserEntity entity) {
        return VKUser.builder()
                .userId(entity.getUser().getId().toString())
                .vkId(entity.getVkId())
                .build();
    }

    private VKUserEntity toEntity(VKUser user) {
        VKUserEntity entity = new VKUserEntity();
        UserEntity userRef = em.getReference(UserEntity.class, UUID.fromString(user.userId()));
        entity.setUser(userRef);
        entity.setVkId(user.vkId());
        return entity;
    }
}
