package smpp.utils;

/**
 * Created by luyenchu on 6/28/16.
 */
public class Telco {
    public static String normalizeSubId(String input) {
        if (input.startsWith("+")) {
            input = input.substring(1);
        }
        if (input.startsWith("84")) {
            input = input.substring(2);
        }
        if (input.startsWith("0")) {
            input = input.substring(1);
        }
        return input;
    }
}
