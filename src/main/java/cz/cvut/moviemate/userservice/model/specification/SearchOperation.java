package cz.cvut.moviemate.userservice.model.specification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum SearchOperation {
    EQUAL(':'),
    NEGATION('!'),
    CONTAIN('*'),
    LIKE('~'),
    GREATER_THAN('<'),
    LESS_THAN('>');

    private static final Map<Character, SearchOperation> OPERATION_MAP = Stream
            .of(SearchOperation.values())
            .collect(Collectors.toMap(SearchOperation::getOpChar, operation -> operation));
    private final char opChar;

    public static SearchOperation getOperation(char opChar) {
        return OPERATION_MAP.getOrDefault(opChar, null);
    }

    public static String getRegexOfAllOperations() {
        return Arrays.stream(SearchOperation.values())
                .map(operation -> String.valueOf(operation.getOpChar()))
                .collect(Collectors.joining("", "([", "])"));
    }
}
