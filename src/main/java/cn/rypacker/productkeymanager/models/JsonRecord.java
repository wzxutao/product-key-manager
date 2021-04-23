package cn.rypacker.productkeymanager.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class JsonRecord {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String jsonString;

    private String productKey;

    private final long CREATED_MILLI = System.currentTimeMillis();

    public enum ProductKeyContentType {
        ALL, ID
    }

    @Enumerated(EnumType.STRING)
    private ProductKeyContentType keyType;


    public JsonRecord(String jsonString) {
        this.jsonString = jsonString;
    }

    public JsonRecord(String jsonString, String productKey, ProductKeyContentType keyType) {
        this.jsonString = Objects.requireNonNull(jsonString);
        this.productKey = Objects.requireNonNull(productKey);
        this.keyType = Objects.requireNonNull(keyType);
    }

    public JsonRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public long getCREATED_MILLI() {
        return CREATED_MILLI;
    }

    public ProductKeyContentType getKeyType() {
        return keyType;
    }

    public void setKeyType(ProductKeyContentType keyType) {
        this.keyType = keyType;
    }

    @Override
    public String toString() {
        return "JsonRecord{" +
                "id=" + id +
                ", jsonString='" + jsonString + '\'' +
                ", productKey='" + productKey + '\'' +
                ", CREATED_MILLI=" + CREATED_MILLI +
                ", keyType=" + keyType +
                '}';
    }
}
