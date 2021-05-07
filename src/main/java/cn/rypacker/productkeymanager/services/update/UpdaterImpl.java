package cn.rypacker.productkeymanager.services.update;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class UpdaterImpl implements Updater {

    private Document document;
    private static final Logger logger = LoggerFactory.getLogger(UpdaterImpl.class);

    /**
     * returns positive number if v > other, 0 if equal, negative number if v > other
     * @param v
     * @param other
     * @return
     * @throws IllegalArgumentException if either version string is not in the correct format
     */
    public int compareVersion(String v, String other){
        final var DELIMITERS = "[.,:-]";
        var v1a = v.split(DELIMITERS);
        var v2a = other.split(DELIMITERS);
        if(v1a.length != 3 || v2a.length != 3){
            throw new IllegalArgumentException(String.format("" +
                    "%s or %s is not in the format ?.?.?\n", v, other));
        }

        for(int i=0; i<v1a.length; i++){
            try{
                var v1Frag = Integer.parseInt(v1a[i]);
                var v2Frag = Integer.parseInt(v2a[i]);
                if(v1Frag != v2Frag){
                    return v1Frag - v2Frag;
                }

            }catch (NumberFormatException e){
                throw new IllegalArgumentException(e);
            }
        }
        return 0;
    }


    public String retrieveLatestVersionFromGithub() throws IOException {
        final var LATEST_RELEASE_URL =
                "https://github.com/wzxutao/product-key-manager/releases/latest";

        var doc = Jsoup.connect(LATEST_RELEASE_URL).get();
        this.document = doc;
        var title = doc.select(".release-header a").text();
        var matcher = Pattern.compile("(?<=product-key-manager-)\\d+\\.\\d+\\.\\d+")
                .matcher(title);
        if (!matcher.find()){
            return null;
        }

        return matcher.group();
    }

    /**
     * this method should be called after version check as it assumes document has been fetched
     * @return
     * @throws IllegalArgumentException if {@link #isLatestVersion()} has not been called
     */
    public String retrieveLatestZipHref(){
        if(document == null){
            throw new IllegalStateException("document not retrieved," +
                    " you should check version first");
        }
        return document.select(".release-header~details a").
                attr("abs:href");
    }

    @Override
    public boolean isLatestVersion() throws IOException{
        var currentVersion = StaticInformation.VERSION_NUMBER;
        logger.info("checking version with github...");
        var latestVersion = retrieveLatestVersionFromGithub();
        logger.info("latest version: " + latestVersion);
        return compareVersion(currentVersion, latestVersion) >= 0;

    }

    private Path downloadZip(String href) throws IOException {
        logger.info("downloading update...");
        var saveTo = Path.of(StaticInformation.TEMP_DIR, "update.zip");
        FileSystemUtil.download(href, saveTo);
        logger.info("update zip downloaded to: " + saveTo);
        return saveTo;
    }

    private Path unzip(Path zip) throws IOException {
        var unzipTo = Path.of(StaticInformation.TEMP_DIR,
                "update" + System.currentTimeMillis());
        FileSystemUtil.unzip(zip, unzipTo);
        logger.info("update unzipped to: " + unzipTo.toString());
        return unzipTo;
    }

    private void replaceExisting(Path unzippedUpdate)
            throws IOException,UpdateFailedException {
        Path contentsDir;
        try{
            contentsDir = unzippedUpdate.toFile().listFiles()[0].toPath();
        }catch (NullPointerException e){
            throw new UpdateFailedException(e);
        }
        var currentDir = System.getProperty("user.dir");
//        FileSystemUtil.copyFolder(contentsDir,
//                Path.of(currentDir));
        Runtime.getRuntime().exec(String.format(
                "cmd /c start \"\" a_update.bat %s %s", contentsDir, currentDir
        ));
        System.exit(0);
    }



    /**
     * should be called after {@link #isLatestVersion()}
     */
    @Override
    public void update() throws UpdateFailedException {
        try{
            var zipHref = retrieveLatestZipHref();
            var zipPath = downloadZip(zipHref);
            var unzippedTo = unzip(zipPath);
            replaceExisting(unzippedTo);
        }catch (IOException e){
            throw new UpdateFailedException(e);
        }
    }

    public static void main(String[] args) throws IOException, UpdateFailedException {
        var upd = new UpdaterImpl();
        upd.isLatestVersion();
        upd.update();
    }
}
