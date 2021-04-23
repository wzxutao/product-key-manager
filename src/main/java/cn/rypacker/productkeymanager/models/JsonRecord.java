package cn.rypacker.productkeymanager.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JsonRecord {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String jsonString;


    public JsonRecord(String jsonString) {
        this.jsonString = jsonString;
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

    @Override
    public String toString() {
        return "JsonRecord{" +
                "id=" + id +
                ", jsonString='" + jsonString + '\'' +
                '}';
    }
}
