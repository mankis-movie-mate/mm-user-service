package cz.cvut.moviemate.userservice.model.specification.handler;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

public class LessThanHandler<T> extends Handler<T> {

    @Override
    public Predicate handle(From<T, ?> root, String key, String value, CriteriaBuilder cb) {
        return cb.lessThan(root.get(key).as(String.class), value);
    }
}
