// NGramGameAlbanian.java
// Part I: N-gram entropy on Albanian text
// Bonus : bigram language identifier (Albanian vs English)


import java.io.*;
import java.text.Normalizer;
import java.util.*;


public class NGramGameAlbanian {
    public static void main(String[] args) throws IOException {
        // --- PART I: N-GRAM ENTROPY ---
        int maxSize = 10;
        File dataDir = new File("src/data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.err.println("Missing 'data' folder at project root.");
            return;
        }

        File[] files = dataDir.listFiles((d,name) -> name.toLowerCase().endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.err.println("No .txt files found in data/");
            return;
        }

        for (File f : files) {
            System.out.println("=== Processing N-grams: " + f.getName() + " ===");
            // read + normalize whole file
            StringBuilder sb = new StringBuilder();
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
                String line;
                while ((line = r.readLine()) != null) {
                    line = Normalizer.normalize(line, Normalizer.Form.NFC)
                            .toLowerCase();
                    for (char c : line.toCharArray()) {
                        if (Character.isLetter(c)) sb.append(c);
                        else sb.append(' ');
                    }
                    sb.append(' ');
                }
            }
            String text = sb.toString().trim().replaceAll(" +", " ");

            // for n = 0…maxSize
            for (int n = 0; n <= maxSize; n++) {
                System.out.println("\nn = " + n + "-gram");
                Map<String,Integer> counts = new HashMap<>();

                if (n == 0) {
                    // uniform over unique letters
                    Set<Character> seen = new HashSet<>();
                    for (char c : text.toCharArray()) {
                        if (Character.isLetter(c)) seen.add(c);
                    }
                    for (char c : seen) counts.put("" + c, 1);
                } else {
                    // slide over letters-only
                    String lettersOnly = text.replace(" ","");
                    for (int i = 0; i + n <= lettersOnly.length(); i++) {
                        String tok = lettersOnly.substring(i, i + n);
                        counts.put(tok, counts.getOrDefault(tok,0) + 1);
                    }
                }

                // compute entropy
                int total = 0;
                for (int c : counts.values()) total += c;
                double H = 0;
                for (int c : counts.values()) {
                    double p = (double)c / total;
                    H -= p * (Math.log(p) / Math.log(2));
                }
                System.out.printf("Entropy: %.6f bits%n", H);
                System.out.println("Total tokens: " + total);

                // sort by freq
                List<Map.Entry<String,Integer>> list =
                        new ArrayList<>(counts.entrySet());
                list.sort((a,b) -> b.getValue() - a.getValue());

                // top 5
                System.out.println("Top 5 tokens:");
                for (int i = 0; i < 5 && i < list.size(); i++) {
                    System.out.println("  " + list.get(i).getKey()
                            + " : " + list.get(i).getValue());
                }
                // bottom 5
                System.out.println("Bottom 5 tokens:");
                int start = Math.max(0, list.size() - 5);
                for (int i = start; i < list.size(); i++) {
                    System.out.println("  " + list.get(i).getKey()
                            + " : " + list.get(i).getValue());
                }
            }
            System.out.println("=== Done N-grams: " + f.getName() + " ===\n");
        }

        // --- BONUS PART: LANGUAGE IDENTIFIER ---
        System.out.println("=== Language Identifier ===");
        runLanguageIdentifier();
    }

    // ----------------------------------------------------------------
    // BONUS: identify whether input.txt is Albanian or English
    // based on bigram log-probs with add-one smoothing.
    static void runLanguageIdentifier() throws IOException {

        // read & clean corpora
        String alb = cleanText(readFile("src/data2/alb2.txt"));
        String eng = cleanText(readFile("src/data2/english.txt"));

        // read raw input
        String raw = readFile("src/data2/input.txt");
        String inp = cleanText(raw);

        Map<String,Integer> albModel = buildBigramModel(alb);
        Map<String,Integer> engModel = buildBigramModel(eng);
        Set<Character> albChars = uniqueChars(alb);
        Set<Character> engChars = uniqueChars(eng);

        double logAlb = computeLogProbability(inp, albModel, albChars);
        double logEng = computeLogProbability(inp, engModel, engChars);
        System.out.println("Input sentence: " + raw);
        System.out.println("LogProb (Albanian): " + logAlb);
        System.out.println("LogProb (English) : " + logEng);
        System.out.println("Likely Language: "
                + (logAlb > logEng ? "Albanian" : "English"));
    }

    // read entire file as UTF-8 string
    static String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    // keep only a–z plus ë,ç
    static String cleanText(String text) {
        return text.toLowerCase().replaceAll("[^a-zëç]", "");
    }

    // build bigram counts
    static Map<String,Integer> buildBigramModel(String text) {
        Map<String,Integer> m = new HashMap<>();
        for (int i = 0; i + 2 <= text.length(); i++) {
            String bg = text.substring(i, i+2);
            m.put(bg, m.getOrDefault(bg,0) + 1);
        }
        return m;
    }
    // collect unique characters
    static Set<Character> uniqueChars(String text) {
        Set<Character> s = new HashSet<>();
        for (char c : text.toCharArray()) s.add(c);
        return s;
    }

    // compute log₂ probability with add-one smoothing
    static double computeLogProbability(
            String sent,
            Map<String,Integer> model,
            Set<Character> chars

    ) {
        if (sent.length() < 2) return Double.NEGATIVE_INFINITY;
        int total = 0;
        for (int c : model.values()) total += c;
        int V = chars.size() * chars.size();  // bigram vocab
        double logp = 0;
        for (int i = 0; i + 2 <= sent.length(); i++) {
            String bg = sent.substring(i, i+2);
            int cnt = model.getOrDefault(bg, 0);
            double prob = (cnt + 1.0) / (total + V);
            logp += Math.log(prob) / Math.log(2);
        }
        return logp;
    }
}