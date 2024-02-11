package cn.rypacker.productkeymanager.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    @NotEmpty
    public String username;

    @NotEmpty
    public String password;
}
