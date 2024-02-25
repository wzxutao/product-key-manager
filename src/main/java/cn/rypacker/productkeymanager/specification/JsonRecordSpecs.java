package cn.rypacker.productkeymanager.specification;

import cn.rypacker.productkeymanager.entity.JsonRecord;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static cn.rypacker.productkeymanager.common.Constants.RECORD_KEY_USERNAME;

public class JsonRecordSpecs {

    private static final String usernameKey = String.format("$.%s[0]", RECORD_KEY_USERNAME);

    public static Specification<JsonRecord> productKeyIn(List<String> productKeys) {
        return (root, query, builder) -> root.get("productKey").in(productKeys);
    }

    public static Specification<JsonRecord> productKeyContains(String string) {
        return (root, query, builder) -> builder.like(root.get("productKey"), "%" + string + "%");
    }

    public static Specification<JsonRecord> createdMilliBetween(Long from, Long to) {
        return (root, query, builder) -> builder.between(root.get("createdMilli"), from, to);
    }

    public static Specification<JsonRecord> createdMilliAfter(Long from) {
        return (root, query, builder) -> builder.ge(root.get("createdMilli"), from);
    }

    public static Specification<JsonRecord> createdMilliBefore(Long to) {
        return (root, query, builder) -> builder.le(root.get("createdMilli"), to);
    }


    public static Specification<JsonRecord> usernameEquals(String username) {
        return (root, query, builder) -> builder.equal(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal(usernameKey)),
                username);
    }

    public static Specification<JsonRecord> usernameNotEquals(String username) {
        return (root, query, builder) -> builder.notEqual(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal(usernameKey)),
                username);
    }

    public static Specification<JsonRecord> usernameContains(String substring) {
        return (root, query, builder) -> builder.like(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal(usernameKey)),
                "%" + substring + "%");
    }

    public static Specification<JsonRecord> statusEquals(int status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<JsonRecord> statusNotEquals(int status) {
        return (root, query, builder) -> builder.notEqual(root.get("status"), status);
    }


    public static Specification<JsonRecord> payloadContains(String substring) {
        return (root, query, builder) -> builder.like(root.get("jsonString"), "%" + substring + "%");
    }

    public static Specification<JsonRecord> payloadNotContains(String substring) {
        return (root, query, builder) -> builder.notLike(root.get("jsonString"), "%" + substring + "%");
    }

    public static Specification<JsonRecord> fieldEquals(String field, String value) {
        return (root, query, builder) -> builder.equal(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal("$." + field + "[0]")),
                value);
    }

    public static Specification<JsonRecord> fieldContains(String field, String substring) {
        return (root, query, builder) -> builder.like(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal("$." + field + "[0]")),
                "%" + substring + "%");
    }

    public static Specification<JsonRecord> fieldNotContains(String field, String substring) {
        return (root, query, builder) -> builder.notLike(builder.function("json_extract",
                        String.class,
                        root.get("jsonString"),
                        builder.literal("$." + field + "[0]")),
                "%" + substring + "%");
    }
}
