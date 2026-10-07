package org.json;

/*
Public Domain.
*/

/**
 * The HTTPTokener extends the JSONTokener to provide additional methods
 * for the parsing of HTTP headers.
 * 
 * @author JSON.org
 * @version 2015-12-09
 */
public class HTTPTokener extends JSONTokener {

    /**
     * Construct an HTTPTokener from a string.
     * 
     * @param string A source string.
     */
    public HTTPTokener(String string) {
        super(string);
    }

    /**
     * Get the next token or string. This is used in parsing HTTP headers.
     * 
     * @return A String.
     * @throws JSONException if a syntax error occurs
     */
    public String nextToken() throws JSONException {
        char c;
        char q;
        StringBuilder sb = new StringBuilder();
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == '"' || c == '\'') {
            q = c;
            return parseQuotedToken(q);
        }
        for (;;) {
            if (c == 0 || Character.isWhitespace(c)) {
                return sb.toString();
            }
            sb.append(c);
            c = next();
        }
    }

    /**
     * Parse a quoted token (string or value enclosed in quotes).
     * 
     * @param q The quote character (" or ')
     * @return The quoted string content (without quotes)
     * @throws JSONException if the quoted string is unterminated
     */
    private String parseQuotedToken(char q) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (;;) {
            char c = next();
            if (c < ' ') {
                throw syntaxError("Unterminated string.");
            }
            if (c == q) {
                return sb.toString();
            }
            sb.append(c);
        }
    }
}
