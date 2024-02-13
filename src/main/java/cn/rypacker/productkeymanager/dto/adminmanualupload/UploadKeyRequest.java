package cn.rypacker.productkeymanager.dto.adminmanualupload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadKeyRequest {

    @NotEmpty
    private String productKey;

    private String username;

    @NotNull
    private Map<String, String> data;
}
