package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * תרגיל 7: תסריט חישוב דמי אבטלה
 * 
 * תהליך הבדיקה:
 * 1. כניסה לאתר ביטוח לאומי
 * 2. ניווט: קצבאות והטבות -> אבטלה
 * 3. לחיצה על "מחשבוני דמי אבטלה"
 * 4. כניסה ל"חישוב סכום דמי אבטלה"
 * 5. מילוי תאריך הפסקת עבודה (עד חודש אחורה)
 * 6. בחירת גיל מעל 28
 * 7. מילוי סכומי השתכרות
 * 8. בדיקה שמופיעים: שכר יומי ממוצע, דמי אבטלה ליום, דמי אבטלה לחודש
 */
public class Test7_UnemploymentCalculator {
    
    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("--remote-allow-origins=*");
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
                    String fileName = "Error_Test7_" + System.currentTimeMillis() + ".png";
                    FileUtils.copyFile(screenshot, new File("screenshots/" + fileName));
                    System.out.println("!!! צילום מסך נשמר: screenshots/" + fileName);
                } catch (IOException e) {
                    System.out.println("נכשל בשמירת צילום מסך: " + e.getMessage());
                }
                driver.quit();
            }
        }
        
        @Override
        public void testSuccessful(ExtensionContext context) {
            if (driver != null) {
                driver.quit();
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
    
    @Test
    @DisplayName("תרגיל 7: חישוב דמי אבטלה")
    public void testUnemploymentCalculator() {
        try {
            // שלב 1: כניסה לאתר
            System.out.println("=== שלב 1: כניסה לאתר ביטוח לאומי ===");
            driver.get("https://www.btl.gov.il/Pages/default.aspx");
            HomePage homePage = new HomePage(driver);
            
            // שלב 2: ניווט לדף אבטלה
            System.out.println("=== שלב 2: ניווט לדף אבטלה ===");
            UnemploymentPage unemploymentPage = homePage.navigateToUnemployment();
            
            // בדיקה קטנה שהגענו לדף הנכון
            Thread.sleep(1000);
            String pageTitle = unemploymentPage.getPageTitle();
            System.out.println("כותרת הדף: " + pageTitle);
            assertTrue(pageTitle.contains("אבטלה") || driver.getTitle().contains("אבטלה"),
                      "צריך להגיע לדף אבטלה");
            
            // שלב 3: לחיצה על "מחשבוני דמי אבטלה"
            System.out.println("=== שלב 3: מעבר למחשבוני דמי אבטלה ===");
            unemploymentPage.clickOnCalculatorsLink();
            Thread.sleep(1000);
            
            // שלב 4: כניסה ל"חישוב סכום דמי אבטלה"
            System.out.println("=== שלב 4: כניסה למחשבון סכום דמי אבטלה ===");
            UnemploymentCalculatorPage calculatorPage = unemploymentPage.clickOnAmountCalculator();
            Thread.sleep(1000);
            
            // שלב 5: חישוב תאריך הפסקת עבודה (עד חודש אחורה)
            LocalDate today = LocalDate.now();
            LocalDate stopWorkDate = today.minusDays(20); // 20 ימים אחורה
            String stopWorkDateStr = stopWorkDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println("תאריך הפסקת עבודה: " + stopWorkDateStr);
            
            // שלב 6: מילוי השלב הראשון - תאריך וגיל
            System.out.println("=== שלב 5-6: מילוי תאריך הפסקת עבודה וגיל מעל 28 ===");
            calculatorPage.fillStepOne(stopWorkDateStr, true); // true = מעל 28
            
            // שלב 7: מילוי סכומי השתכרות
            System.out.println("=== שלב 7: מילוי סכומי השתכרות ===");
            int[] salaries = {8000, 8500, 8200, 8300, 8100, 8400};
            calculatorPage.fillSalaries(salaries);
            
            // שלב 8: בדיקת תוצאות
            System.out.println("=== שלב 8: בדיקת תוצאות ===");
            Thread.sleep(2000);
            
            // בדיקה שמופיע דף תוצאות
            assertTrue(calculatorPage.isResultsPageDisplayed(),
                      "צריך להופיע דף תוצאות חישוב");
            
            // בדיקה שמופיעים כל השדות הנדרשים
            boolean hasAllFields = calculatorPage.verifyRequiredFields();
            assertTrue(hasAllFields,
                      "צריך להופיע: שכר יומי ממוצע, דמי אבטלה ליום, דמי אבטלה לחודש");
            
            System.out.println("\n✅ תרגיל 7 הושלם בהצלחה!");
            
        } catch (Exception e) {
            System.err.println("❌ שגיאה בתרגיל 7: " + e.getMessage());
            e.printStackTrace();
            fail("הטסט נכשל: " + e.getMessage());
        }
    }
}
