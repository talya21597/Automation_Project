package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BtlParametrizedTest {
    WebDriver driver;

    // התיקון הקריטי: הוספת ה-Annotation שגורם לדרייבר להיווצר לפני כל טסט
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            if (driver != null) {
                try {
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String fileName = "Error_" + context.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";
                    FileUtils.copyFile(screenshot, new File("screenshots/" + fileName));
                    System.out.println("!!! צילום מסך נשמר בתיקיית screenshots");
                } catch (IOException e) {
                    System.out.println("נכשל בשמירת צילום מסך: " + e.getMessage());
                }
                driver.quit(); // סוגרים כאן אחרי הכישלון והצילום
            }
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            if (driver != null) {
                driver.quit(); // סוגרים בסיום מוצלח
            }
        }

        @Override
        public void testDisabled(ExtensionContext context, Optional<String> reason) {
            if (driver != null) driver.quit();
        }

        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            if (driver != null) driver.quit();
        }
    };

    @ParameterizedTest(name = "בדיקת קצבת {0}")
    @CsvSource({
            "אבטלה, אבטלה",
            "ילדים, ילדים",
            "אזרח ותיק, אזרח ותיק",
            "נכות כללית, נכות כללית",
            "אימהות, אימהות"
    })
    public void testBreadcrumbs(String benefitName, String expectedText) {
        // גלישה לאתר
        driver.get("https://www.btl.gov.il/Pages/default.aspx");

        // יצירת HomePage עם הדרייבר שנוצר ב-setup
        HomePage homePage = new HomePage(driver);

        // לחיצה על התפריט הראשי
        homePage.clickMainMenu(MainMenu.KITZBAOT_VE_HATAVOT);

        // לחיצה על הקישור הספציפי מה-CSV
        driver.findElement(By.linkText(benefitName)).click();

        // קבלת הטקסט מה-Breadcrumbs (משתמש ב-ID שעדכנו ב-BtlBasePage)
        String actualBreadcrumb = homePage.getBreadcrumbsText();
        System.out.println("נתיב שנמצא עבור " + benefitName + ": " + actualBreadcrumb);

        // בדיקה שהטקסט הצפוי מופיע בנתיב
        assertTrue(actualBreadcrumb.contains(expectedText),
                "שגיאה: הנתיב [" + actualBreadcrumb + "] לא מכיל את הטקסט הצפוי: " + expectedText);
    }

    @AfterEach
    public void tearDown() {
        // הריקון כאן תקין כי ה-TestWatcher מטפל בסגירה
    }
}