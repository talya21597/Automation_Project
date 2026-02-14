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

        try {
            // המתנה שהדף יטען
            Thread.sleep(2000);
            
            // נגלול קצת למטה כדי לראות את השאלות
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,400);");
            Thread.sleep(1000);

            // 3. עונים על כל השאלות בשלב ב' - מוצאים את כל כפתורי "לא" הגלויים
            System.out.println("מחפש את כל השאלות...");
            java.util.List<WebElement> allNoLabels = driver.findElements(
                By.xpath("//label[contains(text(),'לא') and not(contains(text(),'כן'))]"));
            
            System.out.println("מצאתי " + allNoLabels.size() + " כפתורי 'לא'");
            
            // עוברים על כל הכפתורים הגלויים ולוחצים עליהם
            int answered = 0;
            for (WebElement noLabel : allNoLabels) {
                try {
                    if (noLabel.isDisplayed()) {
                        js.executeScript("arguments[0].scrollIntoView({block:'center'});", noLabel);
                        Thread.sleep(300);
                        js.executeScript("arguments[0].click();", noLabel);
                        answered++;
                        System.out.println("✅ עניתי 'לא' על שאלה " + answered);
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    // ממשיכים לשאלה הבאה
                }
            }
            
            System.out.println("סה\"כ עניתי על " + answered + " שאלות");
            
            if (answered == 0) {
                throw new Exception("לא מצאתי אף כפתור 'לא' לענות עליו!");
            }

            Thread.sleep(800);
            
            // 3.5. גלילה נוספת למטה לכפתור המשך
            js.executeScript("window.scrollBy(0,600);");
            Thread.sleep(1000);

            // 4. חיפוש וה לחיצה על כפתור "המשך" באופן דינמי
            System.out.println("מחפש כפתור המשך...");
            
            WebElement continueBtn = null;
            
            // ניסיון 1: כפתור המשך
            try {
                continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'המשך')] | //input[@type='submit' and contains(@value,'המשך')]")));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
                Thread.sleep(300);
                js.executeScript("arguments[0].click();", continueBtn);
                System.out.println("✅ לחצתי על 'המשך' (ניסיון 1)");
            } catch (Exception e1) {
                // ניסיון 2: כפתור עם מילה "חישוב"
                try {
                    continueBtn = driver.findElement(By.xpath("//button[contains(.,'חישוב')] | //input[contains(@value,'חישוב')]"));
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
                    Thread.sleep(300);
                    js.executeScript("arguments[0].click();", continueBtn);
                    System.out.println("✅ לחצתי על 'חישוב' (ניסיון 2)");
                } catch (Exception e2) {
                    System.err.println("❌ לא מצאתי כפתור המשך!");
                    throw new Exception("לא מצאתי כפתור להמשך");
                }
            }
            
            // המתנה לטעינת תוצאות
            Thread.sleep(3000);
            
            System.out.println("✅ שלב ב' הושלם בהצלחה");

        } catch (Exception e) {
            System.err.println("❌ שגיאה בשלב ב': " + e.getMessage());
            throw new RuntimeException("שלב ב' נכשל", e);
        }
    }

    /**
     * פונקציית עזר: עונה "לא" על שאלה
     */
    private void answerNoToQuestion() throws Exception {
        WebElement noRadio = null;
        
        // נסיון 1: XPath פשוט עם contains
        try {
            noRadio = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(),'לא') and not(ancestor::*[contains(@style,'display: none')])]")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", noRadio);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noRadio);
            System.out.println("✅ סימנתי 'לא' (אפשרות 1)");
            return;
        } catch (Exception e1) {
            // ממשיכים לנסיון הבא
        }
        
        // נסיון 2: מחפש input radio עם value
        try {
            noRadio = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[type='radio'][value*='No']:not([style*='display: none']), input[type='radio'][value='2']:not([style*='display: none'])")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", noRadio);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noRadio);
            System.out.println("✅ סימנתי 'לא' (אפשרות 2)");
            return;
        } catch (Exception e2) {
            // ממשיכים לנסיון הבא
        }
        
        // נסיון 3: XPath מתקדם - מחפש את ה-label "לא" הראשון שגלוי
        try {
            noRadio = driver.findElement(
                By.xpath("(//label[contains(.,'לא') and not(contains(.,'כן'))])[1]")
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", noRadio);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noRadio);
            System.out.println("✅ סימנתי 'לא' (אפשרות 3)");
            return;
        } catch (Exception e3) {
            throw new Exception("לא הצלחתי למצוא כפתור 'לא'");
        }
    }

    /**
     * פונקציית עזר: לוחצת על כפתור המשך
     */
    private void clickNextButton() throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", nextButton
            );
            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
            System.out.println("✅ לחצתי על 'המשך'");
        } catch (Exception e) {
            System.err.println("❌ שגיאה בלחיצה על המשך: " + e.getMessage());
            throw e;
        }
    }
    
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1"))).getText();
    }
    
    public String getStepTwoTitle() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
        return driver.findElement(By.tagName("h2")).getText();
    }
    
    public boolean isFinalStep() {
        try {
            Thread.sleep(1000);
            String bodyText = driver.findElement(By.tagName("body")).getText();
            return bodyText.contains("סיכום") || bodyText.contains("תוצאות") || 
                   bodyText.contains("סך הכל") || bodyText.contains("דמי ביטוח");
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean verifySpecificResults() {
        String result = getResultSummary();
        boolean socialInsurance = result.contains("43");
        boolean healthInsurance = result.contains("120");
        boolean total = result.contains("163");
        
        System.out.println("בדיקת תוצאות:");
        System.out.println("דמי ביטוח לאומי (43): " + (socialInsurance ? "✅" : "❌"));
        System.out.println("דמי ביטוח בריאות (120): " + (healthInsurance ? "✅" : "❌"));
        System.out.println("סך הכל (163): " + (total ? "✅" : "❌"));
        
        return socialInsurance && healthInsurance && total;
    }
    
    public String getResultSummary() {
        // שליפת טקסט התוצאה הסופית
        try {
            WebElement resultDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'Summary')] | //div[contains(@class,'Result')] | //body")));
            String text = resultDiv.getText();
            System.out.println("=== תוצאות שנמצאו (500 תווים ראשונים) ===");
            System.out.println(text.substring(0, Math.min(500, text.length())));
            System.out.println("=== סוף ===");
            return text;
        } catch (Exception e) {
            System.err.println("שגיאה בקריאת תוצאות: " + e.getMessage());
            return "";
        }
    }
}