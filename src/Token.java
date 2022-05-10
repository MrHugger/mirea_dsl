public class Token {

    private final String type;
    private final String token;

    public Token(String type, String token) {
        this.type = type;
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return type + " " + token;
    }
}
