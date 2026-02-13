package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BtlTest {
    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        try {
            // שלב 1 ו-2 (חיפוש ומעבר לסניפים - עובד לך כבר)
            driver.get("https://www.btl.gov.il/Pages/default.aspx");
            HomePage homePage = new HomePage(driver);
            homePage.search("חישוב סכום דמי לידה ליום");

            driver.get("https://www.btl.gov.il/Pages/default.aspx");
            BranchesPage branchesPage = homePage.clickBranches();

            // שלב 3: בחירת סניף
            if (branchesPage.getPageTitleText().contains("סניפים")) {
                branchesPage.clickOnFirstBranch(); // הפונקציה החדשה שלנו
            }

            // שלב 4: בדיקת פרטים
            BranchDetailsPage branchDetails = new BranchDetailsPage(driver);
            if (branchDetails.isAllRequiredInfoDisplayed()) {
                System.out.println("✅ הצלחה! הגענו לדף הסניף הנכון והפרטים קיימים.");
            } else {
                System.out.println("❌ כישלון: עדיין לא מוצא את הפרטים.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}