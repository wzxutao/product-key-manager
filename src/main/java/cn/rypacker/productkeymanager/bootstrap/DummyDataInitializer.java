package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DummyDataInitializer implements CommandLineRunner {
    @Autowired
    JsonRecordRepository jsonRecordRepository;


    @Override
    public void run(String... args) throws Exception {
        var json = new JSONObject();
        json.append("id", 1);
        json.append("operator", "boris johnson");
//        System.out.println(json1.toString());
        jsonRecordRepository.save(new JsonRecord(json.toString()));

        json.clear();
        json.append("id", 2);
        json.append("operator", "thereasa may");
//        System.out.println(json1.toString());
        jsonRecordRepository.save(new JsonRecord(json.toString()));

        System.out.println(jsonRecordRepository.findAll());
    }
}
