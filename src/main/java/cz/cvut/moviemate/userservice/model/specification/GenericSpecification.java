package cz.cvut.moviemate.userservice.model.specification;

import cz.cvut.moviemate.userservice.model.specification.handler.*;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class GenericSpecification<T> implements Specification<T> {
    private final transient Map<SearchOperation, Handler<T>> HANDLERS = Map.ofEntries(
            Map.entry(SearchOperation.EQUAL, new EqualHandler<>()),
            Map.entry(SearchOperation.NEGATION, new NegationHandler<>()),
            Map.entry(SearchOperation.LIKE, new LikeHandler<>()),
            Map.entry(SearchOperation.GREATER_THAN, new GreaterThanHandler<>()),
            Map.entry(SearchOperation.LESS_THAN, new LessThanHandler<>()),
            Map.entry(SearchOperation.CONTAIN, new ContainHandler<>())
    );
    private transient SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From<T, ?> from = root;
        String key = criteria.key();
        String value = criteria.value()
                .toString()
                .toLowerCase();

        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            for (int i = 0; i < parts.length - 1; i++)
                from = from.join(parts[i], JoinType.LEFT);
            key = parts[parts.length - 1];
        }

        return handleOperations(from, key, value, cb);
    }

    private Predicate handleOperations(From<T, ?> root, String key, String value, CriteriaBuilder cb) {
        Handler<T> handler = switch (criteria.operation()) {
            case EQUAL -> HANDLERS.get(SearchOperation.EQUAL);
            case NEGATION -> HANDLERS.get(SearchOperation.NEGATION);
            case LIKE -> HANDLERS.get(SearchOperation.LIKE);
            case GREATER_THAN -> HANDLERS.get(SearchOperation.GREATER_THAN);
            case LESS_THAN -> HANDLERS.get(SearchOperation.LESS_THAN);
            case CONTAIN -> HANDLERS.get(SearchOperation.CONTAIN);
        };

        return handler.handle(root, key, value, cb);
    }
}
