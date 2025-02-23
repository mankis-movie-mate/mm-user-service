package cz.cvut.moviemate.userservice.model.specification.handler;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.Arrays;
import java.util.List;

public class ContainHandler<T> extends Handler<T> {

    @Override
    public Predicate handle(From<T, ?> root, String key, String value, CriteriaBuilder cb) {
        Join<T, ?> join = root.join(key);
        List<String> values = Arrays.asList(value.split(","));
        return join.in(values);
    }
}
