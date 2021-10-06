package ru.netology;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardOrderTestSelenide {

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
    }

    // Верное заполнение полей (с SCC селекторами)
    @Test
    public void shouldOrderCardWithCssSelectors() {
        open("http://localhost:9999");
        $("[type='text']").setValue("Иванов Иван");
        $("[type='tel']").setValue("+79896789034");
        $(".checkbox__box").click();
        $("button").click();
        $(".paragraph_theme_alfa-on-white").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    // Неверно заполненное поле для ввода ФИО
    @Test
    public void shouldShowErrorIfIncorrectFillingOfTheFullName() {
        open("http://localhost:9999");
        $("[type='text']").setValue("Ivanov Ivan");
        $("[type='tel']").setValue("+79896789034");
        $(".checkbox__box").click();
        $("button").click();
        $("[data-test-id ='name'] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    // Пустое после для вводо ФИО
    @Test
    public void shouldShowErrorIfEmptyFieldWithFullName() {
        open("http://localhost:9999");
        $("[type='text']").setValue("");
        $("[type='tel']").setValue("+79896789034");
        $(".checkbox__box").click();
        $("button").click();
        $("[data-test-id ='name'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    // Неверно заполненное после для ввода номера телефоно
    @Test
    public void shouldShowErrorIfIncorrectFillingOfThePhoneNumber() {
        open("http://localhost:9999");
        $("[type='text']").setValue("Иванов Иван");
        $("[type='tel']").setValue("9896789034");
        $(".checkbox__box").click();
        $("button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    // Пустое поле для ввода номера телефона
    @Test
    public void shouldShowErrorIfEmptyFieldWithPhoneNumber() {
        open("http://localhost:9999");
        $("[type='text']").setValue("Иванов Иван");
        $("[type='tel']").setValue("");
        $(".checkbox__box").click();
        $("button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    // Нет флажка согласия в чек-боксе
    @Test
    public void shouldShowErrorWithoutAgreementInCheckBox() {
        open("http://localhost:9999");
        $("[type='text']").setValue("Иванов Иван");
        $("[type='tel']").setValue("+79012345678");
        $("button").click();
        //$(".input_invalid").should(exist);
    }

}
