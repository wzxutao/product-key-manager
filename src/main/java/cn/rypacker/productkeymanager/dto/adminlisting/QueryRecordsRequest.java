package cn.rypacker.productkeymanager.dto.adminlisting;

import cn.rypacker.productkeymanager.entity.JsonRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static cn.rypacker.productkeymanager.specification.JsonRecordSpecs.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRecordsRequest {

    public enum Operator {
        ROOT,
        CREATED_MILLIS_BETWEEN,
        CREATED_MILLIS_AFTER,
        CREATED_MILLIS_BEFORE,
        USERNAME_EQUALS,
        USERNAME_NOT_EQUALS,
        USERNAME_CONTAINS,
        STATUS_EQUALS,
        STATUS_NOT_EQUALS,
        PAYLOAD_CONTAINS,
        PAYLOAD_NOT_CONTAINS,
        FIELD_EQUALS,
        FIELD_CONTAINS,
        FIELD_NOT_CONTAINS
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Criterion {
        // parent and children are composed by AND, while siblings are composed by OR
        private List<Criterion> children;

        private String operand1;
        private String operand2;

        private Operator operator;

        public Specification<JsonRecord> toSpecs() {
            Specification<JsonRecord> specs = null;
            switch (this.operator) {
                case ROOT:
                    specs = Specification.where(null);
                    break;
                case CREATED_MILLIS_BETWEEN:
                    specs = createdMilliBetween(Long.parseLong(this.operand1), Long.parseLong(this.operand2));
                    break;
                case CREATED_MILLIS_AFTER:
                    specs = createdMilliAfter(Long.parseLong(this.operand1));
                    break;
                case CREATED_MILLIS_BEFORE:
                    specs = createdMilliBefore(Long.parseLong(this.operand1));
                    break;
                case USERNAME_EQUALS:
                    specs = usernameEquals(this.operand1);
                    break;
                case USERNAME_NOT_EQUALS:
                    specs = usernameNotEquals(this.operand1);
                    break;
                case USERNAME_CONTAINS:
                    specs = usernameContains(this.operand1);
                    break;
                case STATUS_EQUALS:
                    specs = statusEquals(Integer.parseInt(this.operand1));
                    break;
                case STATUS_NOT_EQUALS:
                    specs = statusNotEquals(Integer.parseInt(this.operand1));
                    break;
                case PAYLOAD_CONTAINS:
                    specs = payloadContains(this.operand1);
                    break;
                case PAYLOAD_NOT_CONTAINS:
                    specs = payloadNotContains(this.operand1);
                    break;
                case FIELD_EQUALS:
                    specs = fieldEquals(this.operand1, this.operand2);
                    break;
                case FIELD_CONTAINS:
                    specs = fieldContains(this.operand1, this.operand2);
                    break;
                case FIELD_NOT_CONTAINS:
                    specs = fieldNotContains(this.operand1, this.operand2);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported operator: " + this.operator);
            }

            if(this.children == null || this.children.isEmpty()) return specs;

            Specification<JsonRecord> childSpec = Specification.where(null);
            for(var child: children) {
                childSpec = childSpec.or(child.toSpecs());
            }
            return specs.and(childSpec);
        }
    }


    private Criterion criterion;

}
