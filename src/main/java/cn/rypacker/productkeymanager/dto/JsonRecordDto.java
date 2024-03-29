package cn.rypacker.productkeymanager.dto;

import cn.rypacker.productkeymanager.common.RecordStatus;
import cn.rypacker.productkeymanager.entity.JsonRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static cn.rypacker.productkeymanager.common.Constants.RECORD_KEY_COMMENT;
import static cn.rypacker.productkeymanager.common.Constants.RECORD_KEY_USERNAME;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonRecordDto {

    @NotNull
    private Long id;

    private String username;

    private Long createdMilli;

    private String productKey;

    @NotEmpty
    private String Status;

    @NotNull
    private transient Map<String, String> expandedAllFields = new HashMap<>();

    private String comment;

    public static JsonRecordDto fromEntity(JsonRecord entity) {
        var dto = new JsonRecordDto();
        dto.setId(entity.getId());
        dto.setCreatedMilli(entity.getCreatedMilli());
        dto.setProductKey(entity.getProductKey());
        dto.setStatus(RecordStatus.toString(entity.getStatus()));

        // extract fields
        var json = new JSONObject(entity.getJsonString());
        for(var k: json.keySet()) {
            var v = ((JSONArray) json.get(k)).get(0).toString();
            if(k.equals(RECORD_KEY_USERNAME)) {
                dto.setUsername(v);
            }else if(k.equals(RECORD_KEY_COMMENT)){
                dto.setComment(v);
            } else{
                dto.expandedAllFields.put(k, v);
            }
        }
        return dto;
    }
}
