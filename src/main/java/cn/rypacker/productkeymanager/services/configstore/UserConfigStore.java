package cn.rypacker.productkeymanager.services.configstore;

import org.springframework.stereotype.Component;

import static cn.rypacker.productkeymanager.config.StaticInformation.METADATA_FILE_PATH_V1;

@Component
public class UserConfigStore extends AbstractJsonFileStore<UserConfig> {
    public UserConfigStore() {
        super(UserConfig.class);
    }

    @Override
    protected String getFilePath() {
        return METADATA_FILE_PATH_V1;
    }

}
