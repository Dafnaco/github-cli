import com.google.gson.JsonArray;
import de.vandermeer.asciitable.AsciiTable;

import java.util.ArrayList;

public class Asset {

    private String releaseName;
    private String assetName;
    private Integer downloadCount;

    public Asset(String releaseName, String assetName, Integer downloadCount ) {
        this.releaseName = releaseName;
        this.assetName = assetName;
        this.downloadCount = downloadCount;
    }

    public static String getTable(ArrayList<Asset> assets){
        AsciiTable at = new AsciiTable();
        var totalDownload = 0;

        at.addRule();
        at.addRow("releaseName", "assetName", "downloadCount");
        at.addRule();

        for (var asset:assets) {
            at.addRow(asset.releaseName, asset.assetName, asset.downloadCount);
            at.addRule();
            totalDownload += asset.downloadCount;
        }

        at.addRow("total", "", totalDownload);
        at.addRule();
        return at.render();
    }

    public static ArrayList<Asset> fromJson (JsonArray downloadResult){
        var result = new ArrayList<Asset>();
        for (var release : downloadResult){
            var releaseObj = release.getAsJsonObject();
            for (var asset : releaseObj.getAsJsonArray("assets")) {
                var assetObj = asset.getAsJsonObject();
                result.add(new Asset(releaseObj.get("name").getAsString(), assetObj.get("name").getAsString(), assetObj.get("download_count").getAsInt()));
            }
        }
        return result;
    }
}
