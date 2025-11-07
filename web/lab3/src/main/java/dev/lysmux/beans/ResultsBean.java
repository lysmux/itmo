package dev.lysmux.beans;

import dev.lysmux.db.Result;
import dev.lysmux.db.ResultsRepository;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("results")
@SessionScoped
public class ResultsBean implements Serializable {
    @Inject
    private ResultsRepository repository;

    public void addResult(Result result) {
        repository.add(result);
    }

    public void clear() {
        repository.clear();
    }

    public List<Result> getResults() {
        return repository.getAll();
    }
}
