package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.storedfile.StoredFileFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class StoredFileSpecification {

    private StoredFileSpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<StoredFileEntity> from(StoredFileFilter query) {
        return (root, _, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query.fileName())) {
                predicates.add(builder.like(builder.lower(root.get("fileName")), "%" + query.fileName().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(query.type())) {
                predicates.add(builder.equal(root.get("type"), query.type()));
            }
            if (!isNull(query.startDate())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), query.startDate()));
            }
            if (!isNull(query.endDate())) {
                predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), query.endDate()));
            }
            if (StringUtils.hasText(query.extension())) {
                predicates.add(builder.equal(root.get("extension"), query.extension()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
