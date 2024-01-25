package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.dto.adminlisting.QueryRecordsRequest;
import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.configstore.UserConfig;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import cn.rypacker.productkeymanager.specification.JsonRecordSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/listing")
public class AdminListingControllerV2 {

    @Autowired
    private JsonRecordRepository jsonRecordRepository;

    @Autowired
    private UserConfigStore userConfigStore;

    @GetMapping("/query-records")
    public List<JsonRecord> queryRecords(QueryRecordsRequest reqBody) {
        var mandatoryFields = userConfigStore.getData().getRecord().getMandatoryFields();
        return jsonRecordRepository.findAll(JsonRecordSpecs.createdMilliBetween(reqBody.getFromTime(), reqBody.getToTime()))
                .stream()
                .map(record -> record.withFieldsExpanded(mandatoryFields))
                .collect(Collectors.toList());
    }
}
