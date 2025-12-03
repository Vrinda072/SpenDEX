package web;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {
    /**
     * Parses URL query strings into a Map<String, String>
     * @param query the query string (without the "?" prefix)
     * @return a map of query parameters and their values
     */
    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        
        if (query == null || query.isEmpty()) {
            return params;
        }
        
        try {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    params.put(key, value);
                } else if (idx == 0) {
                    // Empty key, skip
                    continue;
                } else {
                    // No value, just key
                    String key = URLDecoder.decode(pair, StandardCharsets.UTF_8);
                    params.put(key, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return params;
    }
}
