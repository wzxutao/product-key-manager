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
}
