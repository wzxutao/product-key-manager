package cn.rypacker.productkeymanager.specification;

import cn.rypacker.productkeymanager.models.JsonRecord;
import org.springframework.data.jpa.domain.Specification;

public class JsonRecordSpecs {

    public static Specification<JsonRecord> createdMilliBetween(Long from, Long to) {
        return (root, query, builder) -> builder.between(root.get("createdMilli"), from, to);
    }

    public static Specification<JsonRecord> statusEquals(int status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<JsonRecord> usernameEquals(String username) {
        return (root, query, builder) -> builder.equal(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal("$.username[0]")),
                username);
    }

    public static Specification<JsonRecord> usernameContains(String substring) {
        return (root, query, builder) -> builder.like(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal("$.username[0]")),
                "%" + substring + "%");
    }

    public static Specification<JsonRecord> payloadContains(String substring) {
        return (root, query, builder) -> builder.like(root.get("jsonString"), "%" + substring + "%");
    }

    public static Specification<JsonRecord> fieldEquals(String field, String value) {
        return (root, query, builder) -> builder.equal(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal("$." + field + "[0]")),
                value);
    }
}
