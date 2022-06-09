import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.vandermeer.asciitable.AsciiTable;

public class Stats {

    private Integer starsCount;
    private Integer forksCount;
    private Integer contributorsCount;
    private String language;

    public Stats(Integer starsCount, Integer forksCount, int contributorsCount, String language){
        this.starsCount = starsCount;
        this.forksCount = forksCount;
        this.contributorsCount = contributorsCount;
        this.language = language;
    }

    public static Stats fromJson(JsonObject statsResult, JsonArray contributorsStats){
        return new Stats(
                statsResult.get("stargazers_count").getAsInt(),
                statsResult.get("forks_count").getAsInt(),
                contributorsStats.size(),statsResult.get("language").getAsString()
        );
    }

    public static String getTable(Stats stat){
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("stars count", stat.starsCount);
        at.addRule();
        at.addRow("forks count", stat.forksCount);
        at.addRule();
        at.addRow("contributors count", stat.contributorsCount);
        at.addRule();
        at.addRow("language", stat.language);
        at.addRule();
        String rend = at.render();
        return rend;
    }
}