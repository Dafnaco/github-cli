import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@CommandLine.Command(name = "githubCLI", subcommands = {CommandLine.HelpCommand.class},
        description = "Print information about each command")

public class MainCLI implements Runnable {
    @Spec
    CommandSpec spec;

    @Option(names = {"-r", "--repo"}, required = true, description = "The repository to analyze")
    private String repo;

    @Option(names = {"-o", "--output"},
            description = "The output path of the txt file")
    private String outputPath = "";

    @Option(names = {"-h", "--help"}, usageHelp = true,
            description = "Print information about each command")
    private boolean helpRequested = false;

    @Command(name = "downloads",
            description = "Present the entire downloads for each asset",
            exitCodeOnInvalidInput = 1,
            exitCodeOnExecutionException = 5)
    void downloads() throws Exception {
        var r = GitHubAPI.getAggregatedDownloads(repo);
        var assets = Asset.fromJson(r);
        if (assets.isEmpty()) {
            System.out.println("No asset for this repository");
            return;
        }
        handleOutput(Asset.getTable(assets));
    }

    @Command(name = "stats",
            description = "Present the stats of the repo (stars, forks, language, contributors)",
            exitCodeOnInvalidInput = 1,
            exitCodeOnExecutionException = 5)
    void stats() throws Exception {
        var r = GitHubAPI.getRepoStats(repo);
        var rc = GitHubAPI.getRepoContributors(repo);

        var stats = Stats.fromJson(r, rc);
        handleOutput(Stats.getTable(stats));
    }
    private void handleOutput(String table) throws IOException {
        if (outputPath != "") {
            File file = new File(outputPath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(table);
            fileWriter.close();
        } else {
            System.out.println(table);
        }
    }

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new MainCLI());
        cmd.setExecutionExceptionHandler(new ExceptionMessageHandler());
        if (args.length == 0) {
            cmd.usage(System.out);
        } else {
            cmd.execute(args);
        }
    }
}
