package dev.lysmux.server.di;

import com.google.inject.AbstractModule;
import dev.lysmux.server.infra.repository.DotsRepository;
import dev.lysmux.server.infra.repository.SQLDotsRepository;

public class RepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DotsRepository.class).to(SQLDotsRepository.class);
    }
}
