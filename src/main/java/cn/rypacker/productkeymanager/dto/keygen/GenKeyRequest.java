package cn.rypacker.productkeymanager.dto.keygen;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenKeyRequest {

    private Integer count;

    @NotNull
    private Map<String, String> data;
}
