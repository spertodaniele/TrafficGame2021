package it.sperto.traffic;

import it.sperto.traffic.game.TrafficGame;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import static java.lang.System.out;

public class App {
    private static final String TXT_SUFFIX = ".txt";

    public static void main(String[] args) throws Exception {
        out.println();
        if (args.length == 0) {
            out.println("The only parameter \" BASE_PATH  \" is missing! pass it as first command line argument!");
        }

        Instant startInstant = Instant.now();
        String BASE_PATH = args[0];

        out.println("BASE_PATH is " + BASE_PATH);
        File baseDir = new File(BASE_PATH);
        File[] baseDirContent = baseDir.listFiles();

        for (File f : baseDirContent) {
            if (f.getName().endsWith(TXT_SUFFIX)) {
                launchGame(f);
            }
        }

        out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        out.println("Heap size " + (Runtime.getRuntime().totalMemory() / 1_000_000) + "MB, elapsed: " + Duration.between(startInstant, Instant.now()).toSeconds() + "s");
        out.println();

        String[] cmds = {"node \"C:\\datasinc\\codice\\googlehashcode\\hash-code-2021-traffic-signaling-score\\index.js\" \"C:\\datasinc\\codice\\TrafficGame2021\\resources\\a_an_example.txt\"",
                "node \"C:\\datasinc\\codice\\googlehashcode\\hash-code-2021-traffic-signaling-score\\index.js\" \"C:\\datasinc\\codice\\TrafficGame2021\\resources\\b_by_the_ocean.txt\"",
                "node \"C:\\datasinc\\codice\\googlehashcode\\hash-code-2021-traffic-signaling-score\\index.js\" \"C:\\datasinc\\codice\\TrafficGame2021\\resources\\c_checkmate.txt\"",
                "node \"C:\\datasinc\\codice\\googlehashcode\\hash-code-2021-traffic-signaling-score\\index.js\" \"C:\\datasinc\\codice\\TrafficGame2021\\resources\\d_daily_commute.txt\"",
                "node \"C:\\datasinc\\codice\\googlehashcode\\hash-code-2021-traffic-signaling-score\\index.js\" \"C:\\datasinc\\codice\\TrafficGame2021\\resources\\e_etoile.txt\"",
                "node \"C:\\datasinc\\codice\\googlehashcode\\hash-code-2021-traffic-signaling-score\\index.js\" \"C:\\datasinc\\codice\\TrafficGame2021\\resources\\f_forever_jammed.txt\""};
        for (String cmd :cmds){
            out.println(execCmd(cmd));
        }

    }

    private static void launchGame(File inputFile) throws Exception {
        Instant start = Instant.now();
        out.println("*****************************" + inputFile.getName() + "*****************************");
        TrafficGame game = new TrafficGame(inputFile);
        String outPath = "undefined";
        try {
            outPath = game.play();
        } catch (Exception e) {
            out.println("Error processing file " + inputFile.getAbsolutePath());
            e.printStackTrace();
        }
        out.println("Heap size " + (Runtime.getRuntime().totalMemory() / 1_000_000) + "MB, elapsed: " + Duration.between(start, Instant.now()).toSeconds() + "s");
    }

    private static String execCmd(String cmd) throws java.io.IOException {
        out.println(cmd);
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
