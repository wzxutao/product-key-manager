package cn.rypacker.productkeymanager.dto.adminlisting;

import cn.rypacker.productkeymanager.models.JsonRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static cn.rypacker.productkeymanager.specification.JsonRecordSpecs.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRecordsRequest {

    public enum Operator {
        CREATED_MILLIS_BETWEEN,
        USERNAME_EQUALS,
        USERNAME_CONTAINS,
        STATUS_EQUALS,
        PAYLOAD_CONTAINS,
        FIELD_EQUALS,

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Criterion {
        private Criterion child;

        private Criterion next;
        private Boolean connectedByAnd;

        private String operand1;
        private String operand2;

        private Operator operator;

        public Specification<JsonRecord> toSpecs() {
            if(this.child != null) { // not leaf node
                return this.child.toSpecs();
            }

            Specification<JsonRecord> specs = null;
            switch (this.operator) {
                case STATUS_EQUALS:
                    specs = statusEquals(Integer.parseInt(this.operand1));
                    break;
                case USERNAME_EQUALS:
                    specs = usernameEquals(this.operand1);
                    break;
                case USERNAME_CONTAINS:
                    specs = usernameContains(this.operand1);
                    break;
                case CREATED_MILLIS_BETWEEN:
                    specs = createdMilliBetween(Long.parseLong(this.operand1), Long.parseLong(this.operand2));
                    break;
                case PAYLOAD_CONTAINS:
                    specs = payloadContains(this.operand1);
                    break;
                case FIELD_EQUALS:
                    specs = fieldEquals(this.operand1, this.operand2);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported operator: " + this.operator);
            }
            if(next == null) return specs;
            if(connectedByAnd == null) {
                throw new IllegalArgumentException("connectedByAnd cannot be null");
            }
            if(connectedByAnd) {
                return specs.and(next.toSpecs());
            } else {
                return specs.or(next.toSpecs());
            }
        }
    }


    private Criterion criterion;

}
