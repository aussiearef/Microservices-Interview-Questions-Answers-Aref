
package coupling;

public class Helper {
    public static void log(String message) {
        System.out.println("[LOG]: " + message);
    }

    public static String formatDate(java.util.Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static boolean isValidEmail(String email) {
        return email.contains("@");
    }
}
