import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Test {
    public static void main(String[] args) {
        java.time.Duration.parse("PT" + "02H59M11" + "S");
        System.out.println("02:59:11".replaceFirst(":","H"));
        String[] t = "#asdasddad".split("#");
    }
}
