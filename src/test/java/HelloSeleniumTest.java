import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class HelloSeleniumTest {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterTest
    public void TearDown() {
        driver.quit();
    }

    //Highlight webelement
    public WebElement findElement(By by) {
        WebElement elem = driver.findElement(by);
        // draw a border around the found element
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", elem);
        }
        return elem;
    }

    @Test
    public void TestOld1() {
        driver.get("https://stylus.ua/");
        driver.manage().window().maximize();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("https://stylus.ua/elektroinstrument/");

//        Из раздела "Электроинструменты" перейти в раздел "Дрели" используя меню
        driver.findElement(By.xpath("//span[@class='watch-all show-more-btn']")).click();
//        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@title='Шлифовальные машины (Болгарки)']")).click();
//        Thread.sleep(2000);

//        Рандомно на страничке выбрать товар
        int count_items = 3;
        List<String> common_regular_price_list = new ArrayList<>();
        List<String> common_old_price_list = new ArrayList<>();
        List<String> individual_regular_price_list = new ArrayList<>();
        List<String> individual_old_price_list = new ArrayList<>();


        if (!driver.findElement(By.xpath("//div[@class='old-price']")).getText().isEmpty()) {
            for (int i = 0; i < count_items; i++) {
                common_regular_price_list.add(driver.findElements(By.xpath("//div[@class='old-price']/../div[@class='regular-price']")).get(i).getText());
                common_old_price_list.add(driver.findElements(By.xpath("//div[@class='old-price']")).get(i).getText());
//                wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//div[@class='old-price']/ancestor::div[@class='content-block']/div/div[@class='name-block']/a")).get(i))).click();
                driver.findElements(By.xpath("//div[@class='old-price']/ancestor::div[@class='content-block']/div/div[@class='name-block']/a")).get(i).click();
                individual_regular_price_list.add(driver.findElement(By.xpath("//div[@class='regular-price']")).getText());
                individual_old_price_list.add(driver.findElement(By.xpath("//div[@class='old-price']")).getText());
//                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
                driver.navigate().back();
            }
        }

        Assert.assertEquals(common_regular_price_list, individual_regular_price_list);
        Assert.assertEquals(common_old_price_list, individual_old_price_list);

        String common_regular_price = driver.findElement(By.xpath("//div[@class='old-price']/../div[@class='regular-price']")).getText();
        String common_old_price = driver.findElement(By.xpath("//div[@class='old-price']")).getText();

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
        driver.findElement(By.xpath("//div[@class='old-price']/ancestor::div[contains(@class, 'content-bloc')]/div/div[2]/a")).click();
//        Провалиться в карточку товара и проверить наличие акционной и старой цены
        Assert.assertNotNull(driver.findElement(By.className("regular-price")), "Regular price doesn't exist");
        Assert.assertNotNull(driver.findElement(By.className("old-price")), "Old price doesn't exist");
        Reporter.log("Promotion price exists");
        System.out.println("SOUT: Promotion price exists");

//        TODO Цены сравнить с ценами на основной страничке

        String individual_regular_price = driver.findElement(By.xpath("//div[@class='center-part']/div[@class='price-block']/div[@class='regular-price']")).getText();
        String individual_old_price = driver.findElement(By.xpath("//div[@class='center-part']/div[@class='price-block']/div[@class='old-price']")).getText();
        Assert.assertEquals(common_regular_price, individual_regular_price);
        Assert.assertEquals(common_old_price, individual_old_price);

//        System.out.println("");
//        TODO Так для 3 - х товаров(выбор количества проверяемых товаров сделать гибким)

    }

    @Test
    public void TestOld2() throws InterruptedException {
        driver.get("https://stylus.ua/");
        driver.manage().window().maximize();
//        Перейти в раздел "Электроинструменты" / "Перфораторы"
        Actions builder = new Actions(driver);
        findElement(By.id("header_catalog"));
        Thread.sleep(500);
        builder.moveToElement(findElement(By.id("header_catalog"))).perform();
        Thread.sleep(500);
        builder.moveToElement(findElement(By.xpath("//a[@title = 'Дача и Сад. Ремонт']"))).perform();
        Thread.sleep(500);
        builder.moveToElement(findElement(By.xpath("//div[@class='category-list']/a[@href='/perforatory/']"))).perform();
        Thread.sleep(500);
        findElement(By.xpath("//div[@class='category-list']/a[@href='/perforatory/']")).click();
//        Проверить, что хотя бы у одного товара есть тикет Акция.
        Assert.assertFalse(driver.findElements(By.cssSelector(".sticker.stock")).isEmpty(), "Sticker stock was not found");
//        System.out.println(" ");
    }

    @Test
    public void TestOld3() throws InterruptedException {
        driver.get("https://stylus.ua/");
        driver.manage().window().maximize();
        // Перейти в раздел "Электроинструменты" / "Болгарки"
        Actions builder = new Actions(driver);
        findElement(By.id("header_catalog"));
        Thread.sleep(500);
        builder.moveToElement(findElement(By.id("header_catalog"))).perform();
        Thread.sleep(500);
        builder.moveToElement(findElement(By.xpath("//a[@title = 'Дача и Сад. Ремонт']"))).perform();
        Thread.sleep(500);
        builder.moveToElement(findElement(By.xpath("//div[@class='category-list']/a[@href='/shlifovalnye-mashiny-bolgarki/']"))).perform();
        Thread.sleep(500);
        findElement(By.xpath("//div[@class='category-list']/a[@href='/shlifovalnye-mashiny-bolgarki/']")).click();

//        Вывести "Наименование" всех товаров у которых есть иконка кредит частями на первых 3х страницах
        findElement(By.className("pagination-block-load-more")).click();
        Thread.sleep(1000);
        findElement(By.className("pagination-block-load-more")).click();
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='month' and text()=3]/../../../../../div/div[@class='name-block']"));
        for (WebElement element : list) {
            System.out.println(element.getText());
        }

        //div[@class='products-listing list']/div

        //System.out.println(" ");

    }

//    @Test
//    public void TestOld4() throws InterruptedException {
//        driver.get("https://stylus.ua/");
//        driver.manage().window().maximize();
//        //TODO В разделе "Электроинструменты" / "Шуруповерты"
//        Actions builder = new Actions(driver);
//        findElement(By.id("header_catalog"));
//        Thread.sleep(500);
//        builder.moveToElement(findElement(By.id("header_catalog"))).perform();
//        Thread.sleep(500);
//        builder.moveToElement(findElement(By.xpath("//a[@title = 'Дача и Сад. Ремонт']"))).perform();
//        Thread.sleep(500);
//        builder.moveToElement(findElement(By.xpath("//div[@class='category-list']/a[@href='/shurupoverty-i-elektrootvertki/']"))).perform();
//        Thread.sleep(500);
//        findElement(By.xpath("//div[@class='category-list']/a[@href='/shurupoverty-i-elektrootvertki/']")).click();
//        //TODO Для 10 рандомных товаров с пометкой "Акция" (может быть % скидки а не Акция) провести расчет акционной цены относительно старой используя процент скидки.
//
//        //TODO В assert упавшего теста вывести наименование товара его ожидаемую и фактическую цену.
//        System.out.println(" ");
//    }


    @Test
    public void Test1() {
        //region Перейти в раздел "Дрели"  используя меню.
        driver.get("https://planeta-instrument.com.ua/");
        driver.manage().window().maximize();

        Actions builder = new Actions(driver);
        builder.moveToElement(findElement(By.xpath("//div[@class='catalog-menu-lvl0-item no-numbers']/a[@href='/catalog/elektroinstrument/']"))).perform();
        builder.moveToElement(findElement(By.xpath("//a[@href='/catalog/dreli/' and @class='menu-lvl1-link ']"))).click().perform();
        //endregion

        //region Рандомно, на страничке выбрать товар, провалиться в карточку товара и проверить наличие акционной и старой цены. Цены сравнить с ценами на основной страничке
        //Так для [count_items] товаров (выбор количества проверяемых товаров сделать гибким)

        findElement(By.xpath("//input[@id='arrFilter_48_2132474510']/../span")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.id("set_filter")));

        List<WebElement> web_href_list = driver.findElements(By.xpath("//div[@class='main-data']/div/a"));

        List<String> href_list = new ArrayList<>();
        for (WebElement webElement : web_href_list) {
            href_list.add(webElement.getAttribute("href"));
        }

        int index;
//        String personal_current_price, personal_old_price, common_page_current_price,
//                common_page_old_price, vendor_code;

        JavascriptExecutor j = (JavascriptExecutor) driver;
//        Вибір кількостітовару count_items
        int count_items = 3;

        for (int i = 0; i < count_items; i++) {
            index = (int) (Math.random() * web_href_list.size());

//            Якщо виконувати напряму driver.get(web_href_list.get(index).getAttribute("href")); відбувається помилка
//            stale element reference: element is not attached to the page document тому виконую через дані, що вже збереженні в змінній
            String personal_current_price = " ";
            String personal_old_price = " ";
            String common_page_current_price = " ";
            String common_page_old_price = " ";
            String vendor_code;

            driver.get(href_list.get(index));

            boolean loaded = false;
            while (!loaded) {
                if (j.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
            }
            WebElement web_vendor_code = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".info.art")));
            vendor_code = web_vendor_code.getText().replace("Артикул: ", "");

            WebElement web_personal_current_price = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='price']/span[@class='value']")));
            personal_current_price = web_personal_current_price.getText();
            //endregion

            if (!driver.findElements(By.xpath("//div[@class='price-values']/span[@class='text']/span/span")).isEmpty()) {
                personal_old_price = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='price-values']/span[@class='text']/span/span"))).getText();
            }
            driver.navigate().back();

            while (!loaded) {
                if (j.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
            }

            while (common_page_current_price.equals(" ")) {
                try {
                    common_page_current_price = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[text()='" + vendor_code + "']/ancestor::div[@class='catalog-item list-item wow fadeIn']/div[@class='buy-block']//span[@class='price']/span"))).getText();
                } catch (Exception e) {
                    WebElement web_btn_click = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='more-catalog-wrap']/a")));
                    web_btn_click.click();
                }
            }
            try {
                WebElement web_common_page_old_price = new WebDriverWait(driver, Duration.ofSeconds(1)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[text()='" + vendor_code + "']/ancestor::div[@class='catalog-item list-item wow fadeIn']/div[@class='buy-block']//span[@class='price-old']/span")));
                common_page_old_price = web_common_page_old_price.getText();
            } catch (Exception e) {

            }

            System.out.println("For vendor сode " + vendor_code);
            System.out.println("personal_current_price: " + personal_current_price.replace(" ", "") + "\ncommon_page_current_price: " + common_page_current_price.replace(" ", ""));
            Assert.assertEquals(personal_current_price, common_page_current_price, "Current price assert");
            if (!personal_old_price.equals(" ")) {
                Assert.assertEquals(personal_old_price, common_page_old_price, "Old price assert");
                System.out.println("personal_old_price: " + personal_old_price.replace(" ", "") + "\ncommon_page_old_price: " + common_page_old_price.replace(" ", ""));
            }

            System.out.println("\n");
        }
        //endregion

    }

    @Test
    public void Test2() {
        driver.get("https://planeta-instrument.com.ua/");
        driver.manage().window().maximize();

        Actions builder = new Actions(driver);
        builder.moveToElement(findElement(By.xpath("//div[@class='catalog-menu-lvl0-item no-numbers']/a[@href='/catalog/elektroinstrument/']"))).perform();
        builder.moveToElement(findElement(By.xpath("//a[@href='/catalog/perforatory/' and @class='menu-lvl1-link ']"))).click().perform();

        Assert.assertFalse(driver.findElements(By.xpath("//div[@class='sticker discount flaticon-sale']")).isEmpty(), "There are no stock items");

    }

    @Test
    public void Test3() {
        driver.get("https://planeta-instrument.com.ua/");
        driver.manage().window().maximize();

        Actions builder = new Actions(driver);
        builder.moveToElement(findElement(By.xpath("//div[@class='catalog-menu-lvl0-item no-numbers']/a[@href='/catalog/elektroinstrument/']"))).perform();
        builder.moveToElement(findElement(By.xpath("//a[@href='/catalog/bolgarki/' and @class='menu-lvl1-link ']"))).click().perform();

        List<WebElement> webElementList = new ArrayList<>();
        int i1 = 0;
        while (i1 != 2) {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='more-catalog-wrap']/a"))).click();
            webElementList.addAll(new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//img[@alt='Оплата частинами']/ancestor::div[@class='main-data']/div/a"))));
            i1++;
        }

        int i = 1;
        for (WebElement element : webElementList) {
            System.out.println(i + ". " + element.getText());
            i++;
        }
    }

    @Test
    public void Test4(){
        //region Description
        driver.get("https://planeta-instrument.com.ua/");
        driver.manage().window().maximize();

        Actions builder = new Actions(driver);
        builder.moveToElement(findElement(By.xpath("//div[@class='catalog-menu-lvl0-item no-numbers']/a[@href='/catalog/elektroinstrument/']"))).perform();
        builder.moveToElement(findElement(By.xpath("//a[@href='/catalog/shurupoverty/' and @class='menu-lvl1-link ']"))).click().perform();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='arrFilter_48_2132474510']/../span"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("set_filter")));
        //endregion

        //TODO Для 10 рандомных товаров с пометкой "Акция" (может быть % скидки а не Акция) провести расчет акционной цены относительно старой используя процент скидки.
        //В assert упавшего теста вывести наименование товара его ожидаемую и фактическую цену.

        List<WebElement> webElementNames = driver.findElements(By.xpath("//div[@class='sticker discount-w-number']//ancestor::div[@class='catalog-item list-item wow fadeIn']/div[@class='main-data']/div/a/span"));
        List<WebElement> webElementsOldPrice = driver.findElements(By.xpath("//div[@class='sticker discount-w-number']//ancestor::div[@class='catalog-item list-item wow fadeIn']//span[@class='price-old']"));
        List<WebElement> webElementsCurrentPriceList = driver.findElements(By.xpath("//div[@class='sticker discount-w-number']//ancestor::div[@class='catalog-item list-item wow fadeIn']//span[@class='price']"));
        List<WebElement> webElementsDiscountStickerlist = driver.findElements(By.cssSelector(".sticker.discount-w-number"));
        List<WebElement> itemsWithDiscount = driver.findElements(By.xpath("//div[@class='sticker discount-w-number']//ancestor::div[@class='catalog-item list-item wow fadeIn']//span[@class='price-old']"));
        List<Integer> discount_list = new ArrayList<>();
        List<Integer> prices_list = new ArrayList<>();
        List<Integer> oldPrices = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String tmp;
        for (WebElement element : webElementNames) {
            tmp = element.getText();
            names.add(tmp);
        }
        for (WebElement element : webElementsDiscountStickerlist) {
            tmp = element.getText().replace("-", "").replace("%", "");
            discount_list.add(Integer.valueOf(tmp));
//            System.out.println(tmp);

        }
        for (WebElement element : webElementsCurrentPriceList) {
            tmp = element.getText().replace(" ", "").replace("грн.", "");
            prices_list.add(Integer.valueOf(tmp));
        }
        for (WebElement element : webElementsOldPrice) {
            tmp = element.getText().replace(" ", "").replace("грн.", "");
            oldPrices.add(Integer.valueOf(tmp));
        }
        for (int i = 0; i < itemsWithDiscount.size(); i++) {
            System.out.println(i + ". " + names.get(i) + "\nЦіна на вітрині: " + prices_list.get(i) + "; Стара ціна: " + oldPrices.get(i) + "; Знижка: " + discount_list.get(i) + "%;" +
                    " Пораховано вручну: " + (oldPrices.get(i) - oldPrices.get(i) * discount_list.get(i) / 100) + ".");

        }
        Assert.assertEquals(prices_list.get(0), oldPrices.get(0) - oldPrices.get(0) * discount_list.get(0) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(1), oldPrices.get(1) - oldPrices.get(1) * discount_list.get(1) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(2), oldPrices.get(2) - oldPrices.get(2) * discount_list.get(2) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(3), oldPrices.get(3) - oldPrices.get(3) * discount_list.get(3) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(4), oldPrices.get(4) - oldPrices.get(4) * discount_list.get(4) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(5), oldPrices.get(5) - oldPrices.get(5) * discount_list.get(5) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(6), oldPrices.get(6) - oldPrices.get(6) * discount_list.get(6) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(7), oldPrices.get(7) - oldPrices.get(7) * discount_list.get(7) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(8), oldPrices.get(8) - oldPrices.get(8) * discount_list.get(8) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");
//        Assert.assertEquals(prices_list.get(9), oldPrices.get(9) - oldPrices.get(9) * discount_list.get(9) / 100, "Ціна за " + names.get(0) + " на вітрині і вручну не співпадають");


    }
}
