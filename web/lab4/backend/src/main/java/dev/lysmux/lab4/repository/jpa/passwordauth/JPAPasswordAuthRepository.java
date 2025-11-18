package dev.lysmux.lab4.repository.jpa.passwordauth;

import dev.lysmux.lab4.auth.providers.password.model.PasswordUser;
import dev.lysmux.lab4.auth.providers.password.repository.PasswordAuthRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
public class JPAPasswordAuthRepository implements PasswordAuthRepository {
    @Override
    public void addUser(PasswordUser user) {
        
    }

    @Override
    public PasswordUser getUser(String username) {
        return null;
    }
}
