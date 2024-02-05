package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.entity.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
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
    @Autowired
    JokeCipher jokeCipher;
    @Autowired
    KeyGenerator keyGenerator;

    @Override
    public void run(String... args) throws Exception {
        var json = new JSONObject();
        json.append("id", 1);
        json.append("operator", "boris johnson");
//        System.out.println(json1.toString());
        var contents = json.toString();
        jsonRecordRepository.save(new JsonRecord(contents, keyGenerator.generateKey()));

        json.clear();
        json.append("id", 2);
        json.append("operator", "thereasa may");
//        System.out.println(json1.toString());
        contents = json.toString();
        jsonRecordRepository.save(new JsonRecord(contents, keyGenerator.generateKey()));

        System.out.println(jsonRecordRepository.findAll());
    }
}
