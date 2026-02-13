package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InsuranceCalculatorPage extends BasePage {

    // בחירת מין זכר - לפי ID (הכי בטוח)
    @FindBy(css = "input[id$='Gender_0']")
    private WebElement maleRadio;

    // איתור לפי הטקסט "המשך" - הכי בטוח באתר הזה
    @FindBy(xpath = "//button[contains(.,'המשך')] | //input[contains(@value,'המשך')]")
    private WebElement nextButton;
    // כפתור המשך - לפי ID (הכי בטוח)



    // מחפש את ה-Label של התשובה "לא" בשאלת קצבת נכות
    @FindBy(xpath = "//label[contains(@for, 'Nacho') and contains(.,'לא')]")
    private WebElement noDisabilityRadio;


    @FindBy(xpath = "//label[contains(.,'תלמיד ישיבה')]")
    private WebElement yeshivaStudentRadio;

    // שדה תאריך לידה - סלקטור גמיש לסיומת ה-ID
    @FindBy(css = "input[id$='BirthDate_Date']")
    private WebElement birthDateInput;





    public InsuranceCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void fillStepOne(String date) {
        // 1. בחירת "תלמיד ישיבה"
        wait.until(ExpectedConditions.elementToBeClickable(yeshivaStudentRadio)).click();

        // 2. הזנת תאריך
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", birthDateInput);
        birthDateInput.clear();
        birthDateInput.sendKeys(date);
        birthDateInput.sendKeys(Keys.TAB);

        // 3. בחירת מין (זכר)
        System.out.println("בוחר מין...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", maleRadio);
        maleRadio.click();

        // --- החלק הקריטי ---
        System.out.println("המתנה קצרה שהכפתור יתעדכן...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        System.out.println("מנסה ללחוץ על המשך...");

        // גלילה לכפתור
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", nextButton);

        // לחיצה באמצעות JavaScript (כי לפעמים ה-Footer מסתיר אותו לסלניום)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
    }

    public void fillStepTwo() {
        System.out.println("נכנס לשלב ב' - קצבת נכות");

        // 1. חזרה לדף הראשי ליתר ביטחון (לפי עקרונות POM במצגת)
        driver.switchTo().defaultContent();

        // 2. המתנה ל-iframe וכניסה אליו (הסביבון של ביטוח לאומי לפעמים לוקח זמן)
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));

        // 3. איתור האלמנט "לא" - נשתמש ב-XPATH הכי פשוט שמופיע במצגת (עמוד 46)
        // הערה: נסי להשתמש בזה כדי למצוא את האלמנט בוודאות
        By noRadioLocator = By.xpath("//label[contains(text(),'לא')]");

        try {
            WebElement noRadio = wait.until(ExpectedConditions.elementToBeClickable(noRadioLocator));

            // 4. גלילה לפי הדוגמה במצגת (עמוד 47)
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", noRadio);

            // 5. לחיצה עם JS (עמוד 47 - "כשלא הצלחנו להקליק בדרך הרגילה")
            js.executeScript("arguments[0].click();", noRadio);

            System.out.println("סימנתי 'לא' בהצלחה.");

            // 6. לחיצה על המשך
            js.executeScript("arguments[0].click();", nextButton);

        } catch (Exception e) {
            System.out.println("שגיאה באיתור הכפתור בשלב ב': " + e.getMessage());
            // אם זה נכשל כאן, כנראה שה-XPATH של ה'לא' צריך להיות ספציפי יותר
        }
    }
    public String getResultSummary() {
        // שליפת טקסט התוצאה הסופית
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'Summary')] | //body")))
                .getText();
    }
}