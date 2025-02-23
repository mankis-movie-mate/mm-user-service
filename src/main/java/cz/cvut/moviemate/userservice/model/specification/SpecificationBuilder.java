package cz.cvut.moviemate.userservice.model.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {
    private final List<SearchCriteria> params;

    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public final void with(String key, String operation, Object value) {
        SearchOperation op = SearchOperation.getOperation(operation.charAt(0));
        if (op != null)
            this.params.add(new SearchCriteria(key, op, value));
    }

    public Specification<T> build() {
        if (this.params.isEmpty())
            return (root, query, cb) -> cb.conjunction();

        Specification<T> result = new GenericSpecification<>(this.params.get(0));
        for (int i = 1; i < this.params.size(); i++)
            result = Specification.where(result).and(new GenericSpecification<>(this.params.get(i)));

        return result;
    }
}
