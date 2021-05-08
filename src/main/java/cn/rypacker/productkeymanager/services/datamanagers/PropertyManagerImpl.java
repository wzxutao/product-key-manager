package cn.rypacker.productkeymanager.services.datamanagers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.springframework.stereotype.Service;


@Service
public class PropertyManagerImpl extends AbstractSerializedMapRepository<String, String>
        implements PropertyManager{

    @Override
    protected String getFilePath() {
        return StaticInformation.METADATA_FILE_PATH;
    }
}
