import miner.HashSolver;
import miner.Pattern;
import miner.PatternParser;
import miner.PrefixSpan;
import parser.CallSeqExtractor;
import parser.entity.SCSFile;
import synthesizer.InnerVarSolver;
import utils.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {

    String seqPath = Config.getSeqPath();

    if (!new File(seqPath).exists()) {
      // Step 1: Extract structured call sequence from corpus
      File projectDir = new File(Config.getRepoCorpusPath());
      CallSeqExtractor.addApis(new ArrayList<String>() {{
         add("setFillPattern");
      }});
      List<SCSFile> scsFiles = CallSeqExtractor.extract(projectDir);

      // Step 2: Hash call sequence to integer list
      HashSolver solver = new HashSolver();
      solver.solve(scsFiles);
    }

    // Step 3: Mine patterns(frequent sub sequences) with PrefixSpan algorithm
    PrefixSpan.mine(seqPath,4, 3);

    // Step 4: Display patterns and fill some holes
    PatternParser patternParser = new PatternParser();
    patternParser.solve();
    for (Pattern pattern: patternParser.getPatterns()) {
      System.out.println(pattern.toString() + "\n");
      InnerVarSolver innerVarSolver = new InnerVarSolver(pattern.content);
      innerVarSolver.solve();
      System.out.println("-----------------------------------------");
    }
  }

}
