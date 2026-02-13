package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BTL {
    public static void main(String[] args) {
        // אתחול הדרייבר
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // כניסה ישירה לדף המחשבון
            driver.get("https://www.btl.gov.il/Simulators/BituahCalc/Pages/Insurance_NotSachir.aspx");

            InsuranceCalculatorPage calculatorPage = new InsuranceCalculatorPage(driver);

            // ביצוע שלב 1: תלמיד ישיבה, תאריך ומין
            System.out.println("מתחיל שלב 1...");
            calculatorPage.fillStepOne("25/01/2000");

            // ביצוע שלב 2: בדיקת קצבת נכות
            System.out.println("מתחיל שלב 2...");
            calculatorPage.fillStepTwo();

            // בדיקת תוצאות
            System.out.println("בודק תוצאות סופיות...");
            String result = calculatorPage.getResultSummary();

            if (result.contains("163")) {
                System.out.println("✅ הטסט עבר! נמצא סכום של 163 ש\"ח.");
            } else {
                System.out.println("❌ הטסט נכשל. התוצאה שהתקבלה: " + result);
            }

        } catch (Exception e) {
            System.err.println("שגיאה במהלך ההרצה: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // driver.quit(); // ניתן להסיר את ה-comment כדי לסגור את הדפדפן בסיום
        }
    }
}
