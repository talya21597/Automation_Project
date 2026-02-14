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
 * תרגיל 6: חישוב דמי ביטוח לאומי לבחור ישיבה
 * 
 * תהליך הבדיקה:
 * 1. כניסה לאתר ביטוח לאומי
 * 2. ניווט: דמי ביטוח -> דמי ביטוח לאומי
 * 3. בדיקת כותרת הדף
 * 4. כניסה למחשבון לחישוב דמי ביטוח
 * 5. מילוי פרטים לתלמיד ישיבה
 * 6. בדיקת התוצאות: 43 ש"ח ביטוח לאומי, 120 ש"ח בריאות, 163 ש"ח סה"כ
 */
public class Test6_InsuranceCalculator {
    
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
                    String fileName = "Error_Test6_" + System.currentTimeMillis() + ".png";
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
    @DisplayName("תרגיל 6: חישוב דמי ביטוח לאומי לבחור ישיבה")
    public void testInsuranceCalculatorForYeshivaStudent() {
        try {
            // שלב 1: כניסה לאתר
            System.out.println("=== שלב 1: כניסה לאתר ביטוח לאומי ===");
            driver.get("https://www.btl.gov.il/Pages/default.aspx");
            HomePage homePage = new HomePage(driver);
            
            // שלב 2: ניווט דרך התפריט לדף "דמי ביטוח לאומי"
            System.out.println("=== שלב 2: ניווט לדף דמי ביטוח לאומי ===");
            InsurancePage insurancePage = homePage.navigateToInsurance();
            
            // שלב 3: בדיקת כותרת הדף
            System.out.println("=== שלב 3: בדיקת כותרת הדף ===");
            String pageTitle = insurancePage.getPageTitleText();
            System.out.println("כותרת הדף: " + pageTitle);
            assertTrue(pageTitle.contains("דמי ביטוח לאומי"), 
                      "כותרת הדף צריכה להכיל 'דמי ביטוח לאומי'");
            
            // שלב 4: כניסה למחשבון לחישוב דמי ביטוח
            System.out.println("=== שלב 4: כניסה למחשבון ===");
            InsuranceCalculatorPage calculatorPage = insurancePage.clickOnCalculatorLink();
            
            // בדיקת כותרת דף המחשבון
            Thread.sleep(1000);
            String calculatorTitle = calculatorPage.getPageTitle();
            System.out.println("כותרת דף המחשבון: " + calculatorTitle);
            assertTrue(calculatorTitle.contains("חישוב דמי ביטוח") || 
                      calculatorTitle.contains("עצמאי") || 
                      calculatorTitle.contains("תלמיד"),
                      "כותרת המחשבון צריכה להכיל מילים רלוונטיות");
            
            // שלב 5: חישוב תאריך לידה (18-70 שנה אחורה)
            LocalDate today = LocalDate.now();
            LocalDate birthDate = today.minusYears(20); // בוחר גיל 20
            String birthDateStr = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println("תאריך לידה: " + birthDateStr);
            
            // צעד ראשון: מילוי פרטים - תלמיד ישיבה
            System.out.println("=== צעד ראשון: מילוי פרטים ===");
            calculatorPage.fillStepOne(birthDateStr);
            
            // צעד שני: קצבת נכות
            System.out.println("=== צעד שני: קצבת נכות ===");
            Thread.sleep(1000);
            calculatorPage.fillStepTwo();
            
            // סיום: בדיקת תוצאות
            System.out.println("=== סיום: בדיקת תוצאות ===");
            Thread.sleep(2000);
            
            // בדיקה שהגענו לשלב הסיום
            assertTrue(calculatorPage.isFinalStep(), 
                      "צריך להגיע לשלב הסיום");
            
            // בדיקת התוצאות הספציפיות
            boolean resultsCorrect = calculatorPage.verifySpecificResults();
            assertTrue(resultsCorrect, 
                      "התוצאות צריכות להכיל: 43 ש\"ח ביטוח לאומי, 120 ש\"ח בריאות, 163 ש\"ח סה\"כ");
            
            System.out.println("\n✅ תרגיל 6 הושלם בהצלחה!");
            
        } catch (Exception e) {
            System.err.println("❌ שגיאה בתרגיל 6: " + e.getMessage());
            e.printStackTrace();
            fail("הטסט נכשל: " + e.getMessage());
        }
    }
}
