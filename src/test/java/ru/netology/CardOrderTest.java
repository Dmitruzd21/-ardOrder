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

    // Верное заполнение полей (примитивный тест)
    @Test
    public void shouldOrderCardWithoutCssSelectors() {
        driver.get("http://localhost:9999");
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Иванов Иван");
        elements.get(1).sendKeys("+79896789034");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualMessage = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    // Верное заполнение полей (с SCC селекторами)
    @Test
    public void shouldOrderCardWithCssSelectors() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79896789034");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualMessage = driver.findElement(By.cssSelector(".paragraph_theme_alfa-on-white")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    // Неверно заполненное поле для ввода ФИО
    @Test
    public void shouldShowErrorIfIncorrectFillingOfTheFullName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79896789034");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id = name] .input__sub")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    // Пустое после для вводо ФИО
    @Test
    public void shouldShowErrorIfEmptyFullNameField() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79896789034");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expectedMessage = "Поле обязательно для заполнения";
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id = name] .input__sub")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    // Неверно заполненное после для ввода номера телефона
    @Test
    public void shouldShowErrorIfIncorrectFillingOfThePhoneNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("9896789034");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    // Пустое поле для ввода номера телефона
    @Test
    public void shouldShowErrorIfEmptyFieldOfPhoneNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expectedMessage = "Поле обязательно для заполнения";
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText();
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    // Нет флажка согласия в чек-боксе
    @Test
    public void shouldShowErrorWithoutAgreementInCheckBox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("button")).click();
        driver.findElement(By.cssSelector(".input_invalid"));
    }
}
