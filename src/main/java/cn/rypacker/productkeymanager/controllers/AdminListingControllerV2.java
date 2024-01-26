package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.dto.JsonRecordDto;
import cn.rypacker.productkeymanager.dto.adminlisting.QueryRecordsRequest;
import cn.rypacker.productkeymanager.exception.IdentifiedWebException;
import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/listing")
public class AdminListingControllerV2 {

    @Autowired
    private JsonRecordRepository jsonRecordRepository;

    @Autowired
    private UserConfigStore userConfigStore;

    @GetMapping("/mandatory-fields")
    public List<String> getMandatoryFields() {
        return userConfigStore.getData().getRecord().getMandatoryFields();
    }

    @PostMapping("/query-records")
    public List<JsonRecordDto> queryRecords(@RequestBody QueryRecordsRequest reqBody) {
        try{
            return jsonRecordRepository.findAll(reqBody.getCriterion() == null ? null : reqBody.getCriterion().toSpecs())
                    .stream()
                    .map(JsonRecordDto::fromEntity)
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException | IllegalArgumentException e) {
            throw new IdentifiedWebException(e.getMessage(), 400);
        }
    }
}
