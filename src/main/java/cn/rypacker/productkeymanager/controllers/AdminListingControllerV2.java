package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.common.RecordStatus;
import cn.rypacker.productkeymanager.dto.JsonRecordDto;
import cn.rypacker.productkeymanager.dto.adminlisting.QueryRecordsRequest;
import cn.rypacker.productkeymanager.entity.JsonRecord;
import cn.rypacker.productkeymanager.exception.IdentifiedWebException;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import cn.rypacker.productkeymanager.specification.JsonRecordSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
        try {
            return jsonRecordRepository.findAll(reqBody.getCriterion() == null ? null : reqBody.getCriterion()
                            .toSpecs()
                            .and(
                                    JsonRecordSpecs.orderByCreatedMillisDesc()
                            ))
                    .stream()
                    .map(JsonRecordDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            throw new IdentifiedWebException(e.getMessage(), 400);
        }
    }

    @GetMapping("/get-by-product-key")
    public JsonRecordDto getByProductKey(@RequestParam("productKey") String productKey) {
        var entity = jsonRecordRepository.findByProductKey(productKey);
        if (entity == null) {
            return null;
        }

        return JsonRecordDto.fromEntity(entity);
    }

    @GetMapping("/quick-search")
    public List<JsonRecordDto> quickSearch(@RequestParam(value = "input", required = false) String keyword) {

        Specification<JsonRecord> specs = Specification.where(null);
        if (keyword != null && !keyword.isBlank()) {
            specs = JsonRecordSpecs.payloadContains(keyword)
                    .or(JsonRecordSpecs.productKeyContains(keyword))
                    .and(JsonRecordSpecs.orderByCreatedMillisDesc())
            ;
        }

        return jsonRecordRepository.findAll(specs)
                .stream()
                .map(JsonRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

}
