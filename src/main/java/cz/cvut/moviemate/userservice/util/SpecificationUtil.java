package cz.cvut.moviemate.userservice.util;

import cz.cvut.moviemate.userservice.model.specification.SearchOperation;
import cz.cvut.moviemate.userservice.model.specification.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SpecificationUtil {
    private static final String OP_REGEX_PATTERN = SearchOperation.getRegexOfAllOperations();
    private static final String QUERY_SEPARATOR = "&";
    private static final String KEY_REGEX_PATTERN = "([\\w.]+?)";
    private static final String VALUE_REGEX_PATTERN = "([\\w- ]+?)" + QUERY_SEPARATOR;

    public <T> Specification<T> buildSpecification(String query) {
        SpecificationBuilder<T> builder = new SpecificationBuilder<>();

        Pattern pattern = Pattern.compile(KEY_REGEX_PATTERN + OP_REGEX_PATTERN + VALUE_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(query + QUERY_SEPARATOR);
        while (matcher.find())
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));

        return builder.build();
    }
}
