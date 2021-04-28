package cn.rypacker.productkeymanager.models;

import cn.rypacker.productkeymanager.services.DatetimeUtil;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class JsonRecord {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String jsonString;

    private String productKey;

    private final long CREATED_MILLI = System.currentTimeMillis();

    private int status = RecordStatus.NORMAL;

    public JsonRecord(String jsonString) {
        this.jsonString = jsonString;
    }

    public JsonRecord(String jsonString, String productKey) {
        this.jsonString = Objects.requireNonNull(jsonString);
        this.productKey = Objects.requireNonNull(productKey);
    }

    public JsonRecord() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCreatedFinalDate(){
        return DatetimeUtil.epochSecondsToFinalDate(CREATED_MILLI / 1000L);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonRecord that = (JsonRecord) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "JsonRecord{" +
                "id=" + id +
                ", jsonString='" + jsonString + '\'' +
                ", productKey='" + productKey + '\'' +
                ", CREATED_MILLI=" + CREATED_MILLI +
                '}';
    }
}
