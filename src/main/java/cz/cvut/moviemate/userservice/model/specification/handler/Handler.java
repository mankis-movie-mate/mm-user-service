package cz.cvut.moviemate.userservice.model.specification.handler;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

public abstract class Handler<T> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    protected Expression<String> keyToLowerKey(From<T, ?> root, String key, CriteriaBuilder cb) {
        return cb.lower(root.get(key).as(String.class));
    }

    protected Expression<String> dateValueToString(Expression<String> key, CriteriaBuilder cb) {
        return cb.function(
                "to_char",
                String.class,
                key,
                cb.literal(DATE_FORMAT)
        );
    }

    public abstract Predicate handle(From<T, ?> root, String key, String value, CriteriaBuilder cb);
}
