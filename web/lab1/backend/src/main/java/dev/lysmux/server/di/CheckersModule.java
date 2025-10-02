package dev.lysmux.server.di;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import dev.lysmux.server.domain.checkers.CircleChecker;
import dev.lysmux.server.domain.checkers.ContainsChecker;
import dev.lysmux.server.domain.checkers.SquareChecker;
import dev.lysmux.server.domain.checkers.TriangleChecker;

public class CheckersModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<ContainsChecker> checkerBinder = Multibinder.newSetBinder(binder(), ContainsChecker.class);
        checkerBinder.addBinding().to(CircleChecker.class);
        checkerBinder.addBinding().to(SquareChecker.class);
        checkerBinder.addBinding().to(TriangleChecker.class);
    }
}
