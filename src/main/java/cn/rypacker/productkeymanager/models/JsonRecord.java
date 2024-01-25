package cn.rypacker.productkeymanager.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name="json_record")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class JsonRecord {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "json_string")
    private String jsonString;

    @Column(name= "product_key")
    private String productKey;

    @Column(name = "created_milli")
    private Long createdMilli = System.currentTimeMillis();

    private int status = RecordStatus.NORMAL;

    private transient Map<String, String> expandedAllFields = new HashMap<>();
    private transient Map<String, String> expandedMandatoryFields = new HashMap<>();

    public JsonRecord(String jsonString) {
        this.jsonString = jsonString;
    }

    public JsonRecord(String jsonString, String productKey) {
        this.jsonString = Objects.requireNonNull(jsonString);
        this.productKey = Objects.requireNonNull(productKey);
    }

    public JsonRecord withFieldsExpanded(List<String> keys) {
        var json = new JSONObject(jsonString);
        for(var k: json.keySet()) {
            if(json.get(k) instanceof JSONArray) {
                expandedAllFields.put(k, ((JSONArray) json.get(k)).get(0).toString());
            }else {
                throw new RuntimeException("field " + k + " is not an array");
            }
        }
        for(var k: keys){
            if(json.has(k)){
                var v = json.get(k);
                if(v instanceof JSONArray) {
                    expandedMandatoryFields.put(k, ((JSONArray) v).get(0).toString());
                }else {
                    throw new RuntimeException("field " + k + " is not an array");
                }
            }
        }
        return this;
    }

}
