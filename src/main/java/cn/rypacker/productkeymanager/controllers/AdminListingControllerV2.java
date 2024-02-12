package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.common.RecordStatus;
import cn.rypacker.productkeymanager.dto.JsonRecordDto;
import cn.rypacker.productkeymanager.dto.adminlisting.QueryRecordsRequest;
import cn.rypacker.productkeymanager.exception.IdentifiedWebException;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.rypacker.productkeymanager.common.Constants.RECORD_KEY_USERNAME;
import static cn.rypacker.productkeymanager.common.Constants.USERNAME_RECORD_VALUE_ADMIN;

@RestController
@RequestMapping("/admin/listing/v2")
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

    @GetMapping("/get-by-product-key")
    public JsonRecordDto getByProductKey(@RequestParam("productKey") String productKey) {
        var entity = jsonRecordRepository.findByProductKey(productKey);
        if(entity == null) {
            return null;
        }

        return JsonRecordDto.fromEntity(entity);
    }

    @PutMapping("/update-record")
    public ResponseEntity<?> updateRecord(@Valid @RequestBody JsonRecordDto dto) {

        var entityOptional = jsonRecordRepository.findById(dto.getId());
        if(entityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var entity = entityOptional.get();
        entity.setStatus(RecordStatus.parse(dto.getStatus()));
        String username = JSONUtil.getFirstValue(entity.getJsonString(), RECORD_KEY_USERNAME);
        dto.getExpandedAllFields().put(RECORD_KEY_USERNAME, username != null ? username : USERNAME_RECORD_VALUE_ADMIN);

        entity.setJsonString(JSONUtil.toStringFrom(dto.getExpandedAllFields()));
        jsonRecordRepository.save(entity);

        return ResponseEntity.ok().build();
    }
}
