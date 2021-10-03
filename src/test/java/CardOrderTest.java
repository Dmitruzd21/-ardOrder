import org.junit.jupiter.api.Test;

public class CardOrderTest {


    @Test
    public void shouldOrderCard () {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }
}
