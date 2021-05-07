package cn.rypacker.productkeymanager.services.update;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdaterImplTest {

    UpdaterImpl updater = new UpdaterImpl();

    @Test
    void compareVersion() {
        assertEquals(updater.compareVersion("0.0.1", "0.0.1"), 0);

        assertTrue(updater.compareVersion("0.0.1", "0.0.2") < 0);
        assertTrue(updater.compareVersion("0.0.3", "0.0.2") > 0);
        assertTrue(updater.compareVersion("0.0.3", "0.0.10") < 0);
        assertTrue(updater.compareVersion("0.0.10", "0.0.10") == 0);
        assertTrue(updater.compareVersion("0.0.11", "0.0.10") > 0);
        assertTrue(updater.compareVersion("0.1.0", "0.0.10") > 0);
        assertTrue(updater.compareVersion("1.0.0", "0.1.10") > 0);
        assertTrue(updater.compareVersion("1.1.20", "1.1.1") > 0);
        assertTrue(updater.compareVersion("1.1.20", "1.1.31") < 0);
        assertTrue(updater.compareVersion("1.1.20", "1.1.3") > 0);

    }

    @Test
    void retrieveVersionAndZipHrefFromGithub(){
        assertDoesNotThrow( () -> {
            var version = updater.retrieveLatestVersionFromGithub();
            assertNotNull(version);
            assertTrue(updater.compareVersion(version, "0.0.0") > 0);

            var zipHref = updater.retrieveLatestZipHref();
            assertNotNull(zipHref);
            assertTrue(zipHref.contains(version));
            assertTrue(zipHref.contains(".zip"));
        });
    }


}