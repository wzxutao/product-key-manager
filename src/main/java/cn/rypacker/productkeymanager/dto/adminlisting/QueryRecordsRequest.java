package cn.rypacker.productkeymanager.dto.adminlisting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRecordsRequest {
    private Long fromTime;
    private Long toTime;

    public Long getFromTime() {
        return fromTime == null ? 0 : fromTime;
    }

    public Long getToTime() {
        return toTime == null ? Long.MAX_VALUE : toTime;
    }
}
