package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;


public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        //указание, что нужно использовать драйвер на локальной машине и путь к нему
        //System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
        //использование этой библиотеки позволяет скачаать драйвер для того же браузера и той же версии, но на linux для CI
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        //Включение headless режима при использовании selenium
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldOrderCard() {
        driver.get("http://localhost:9999");
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Иванов Иван");
        elements.get(1).sendKeys("+79896789034");
        driver.findElement(By.className("checkbox__control")).click();
        driver.findElement(By.className("button")).click();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время";
        String actualMessage = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
