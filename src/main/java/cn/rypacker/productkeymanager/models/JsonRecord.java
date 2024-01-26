package cn.rypacker.productkeymanager.models;

import cn.rypacker.productkeymanager.common.RecordStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public JsonRecord(String jsonString) {
        this.jsonString = jsonString;
    }

    public JsonRecord(String jsonString, String productKey) {
        this.jsonString = Objects.requireNonNull(jsonString);
        this.productKey = Objects.requireNonNull(productKey);
    }

}
