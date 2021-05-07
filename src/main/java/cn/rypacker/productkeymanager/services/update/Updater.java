package cn.rypacker.productkeymanager.services.update;

import java.io.IOException;

public interface Updater {
    boolean isLatestVersion() throws IOException;

    void update() throws UpdateFailedException;
}
