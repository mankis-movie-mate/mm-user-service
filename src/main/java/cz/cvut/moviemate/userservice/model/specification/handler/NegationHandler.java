package cz.cvut.moviemate.userservice.model.specification.handler;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;

public class NegationHandler<T> extends Handler<T> {

    @Override
    public Predicate handle(From<T, ?> root, String key, String value, CriteriaBuilder cb) {
        Class<?> fieldType = root.get(key).getJavaType();
        Expression<String> lowerKey = keyToLowerKey(root, key, cb);

        if (fieldType.equals(LocalDateTime.class)) {
            Expression<String> dateToString = dateValueToString(lowerKey, cb);
            return cb.notEqual(dateToString, value);
        } else if (value.equals("null")) {
            return cb.isNotNull(root.get(key));
        } else {
            return cb.notEqual(root.get(key), value);
        }
    }
}
