import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String testCode;
    private ArrayList<Token> tokens = new ArrayList<>();
    private static Map<String, Pattern> lexems = new HashMap<>();

    public Lexer(String testCode) {
        this.testCode = testCode;
        run();
    }

    static {
        lexems.put("VAR", Pattern.compile("^[a-z_]\\w*$"));
        lexems.put("DIGIT", Pattern.compile("^\\d*$"));
        lexems.put("ASSIGN_OP", Pattern.compile("^=$"));
        lexems.put("OP", Pattern.compile("^(-|\\+|\\*|/)$"));
        lexems.put("L_BC", Pattern.compile("^\\($"));
        lexems.put("R_BC", Pattern.compile("^\\)$"));
        lexems.put("ENDL", Pattern.compile("^;$"));
        lexems.put("COMPARE_OP", Pattern.compile("^(~|<|>|!=)$"));
        lexems.put("IF", Pattern.compile("^IF$"));
        lexems.put("ELSE", Pattern.compile("^ELSE$"));
        lexems.put("WHILE", Pattern.compile("^WHILE$"));
        lexems.put("DO", Pattern.compile("^DO$"));
        lexems.put("FOR", Pattern.compile("^FOR$"));
        lexems.put("DIV", Pattern.compile("^,$"));
    }

    private void run() {
        String tokenStart = "";
        for (int i = 0; i < testCode.length(); i++) {

            if (testCode.toCharArray()[i] == ' ') {
                continue;
            }

            tokenStart += testCode.toCharArray()[i];
            String tokenEnd = " ";

            if (i < testCode.length() - 1) {
                tokenEnd = tokenStart + testCode.toCharArray()[i + 1];
            }

            for (String key: lexems.keySet()) {
                Pattern p = lexems.get(key);
                Matcher m_1 = p.matcher(tokenStart);
                Matcher m_2 = p.matcher(tokenEnd);

                if (m_1.find() && !m_2.find()) {
                    tokens.add(new Token(key, tokenStart));
                    tokenStart = "";
                    break;
                }
            }
        }
        tokens.forEach(System.out::println);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
}
