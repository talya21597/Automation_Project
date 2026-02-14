package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * דף מחשבון דמי אבטלה - מייצג את המחשבון לחישוב דמי אבטלה
 */
public class UnemploymentCalculatorPage extends BasePage {

    public UnemploymentCalculatorPage(WebDriver driver) {
        super(driver);
    }

    /**
     * מחזיר את כותרת הדף
     */
    public String getPageTitle() {
        try {
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
            return driver.findElement(By.tagName("h1")).getText();
        } catch (Exception e) {
            return driver.getTitle();
        }
    }

    /**
     * ממלא את השלב הראשון של המחשבון
     * @param stopWorkDate תאריך הפסקת עבודה בפורמט dd/MM/yyyy
     * @param ageOver28 האם מעל גיל 28
     */
    public void fillStepOne(String stopWorkDate, boolean ageOver28) {
        try {
            System.out.println("ממלא שלב 1: תאריך הפסקת עבודה וגיל");
            
            // מעבר ל-iframe
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
            
            // המתנה לטעינת הדף
            Thread.sleep(1000);
            
            // מילוי תאריך הפסקת עבודה
            WebElement dateField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("input[id*='StopWorkDate'], input[type='text'][id*='Date']")
                    )
            );
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", dateField);
            dateField.clear();
            dateField.sendKeys(stopWorkDate);
            dateField.sendKeys(Keys.TAB);
            
            System.out.println("תאריך הפסקת עבודה הוזן: " + stopWorkDate);
            
            // בחירת גיל
            Thread.sleep(500);
            String ageValue = ageOver28 ? "2" : "1"; // 2 = מעל 28, 1 = עד 28
            WebElement ageRadio = driver.findElement(
                    By.cssSelector("input[type='radio'][value='" + ageValue + "']")
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ageRadio);
            
            System.out.println("גיל נבחר: " + (ageOver28 ? "מעל 28" : "עד 28"));
            
            // לחיצה על המשך
            Thread.sleep(500);
            WebElement nextButton = driver.findElement(
                    By.xpath("//button[contains(.,'המשך')] | //input[contains(@value,'המשך')]")
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", nextButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
            
            System.out.println("שלב 1 הושלם - לחצתי על המשך");
            
        } catch (Exception e) {
            System.err.println("שגיאה במילוי שלב 1: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * ממלא סכומי השתכרות של 6 החודשים האחרונים
     * @param salaries מערך של 6 סכומים
     */
    public void fillSalaries(int[] salaries) {
        try {
            System.out.println("ממלא שלב 2: סכומי השתכרות");
            
            Thread.sleep(1500);
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
            
            // מציאת כל שדות השכר
            for (int i = 0; i < Math.min(6, salaries.length); i++) {
                try {
                    // מחפש שדות קלט עם id המכיל Salary או Wage
                    WebElement salaryField = driver.findElement(
                            By.cssSelector("input[id*='Salary_" + i + "'], input[id*='Wage'][type='text']")
                    );
                    
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block:'center'});", salaryField
                    );
                    salaryField.clear();
                    salaryField.sendKeys(String.valueOf(salaries[i]));
                    
                    System.out.println("חודש " + (i + 1) + ": " + salaries[i] + " ש\"ח");
                    Thread.sleep(300);
                    
                } catch (Exception e) {
                    System.out.println("לא נמצא שדה שכר " + (i + 1) + " - ממשיך");
                }
            }
            
            // לחיצה על המשך/חשב
            Thread.sleep(500);
            WebElement calculateButton = driver.findElement(
                    By.xpath("//button[contains(.,'חשב')] | //button[contains(.,'המשך')] | //input[contains(@value,'חשב')]")
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", calculateButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculateButton);
            
            System.out.println("שלב 2 הושלם - חישוב בוצע");
            
        } catch (Exception e) {
            System.err.println("שגיאה במילוי שכר: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * בודק שמופיע דף תוצאות חישוב
     */
    public boolean isResultsPageDisplayed() {
        try {
            Thread.sleep(2000);
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
            
            String bodyText = driver.findElement(By.tagName("body")).getText();
            return bodyText.contains("תוצאות") || bodyText.contains("חישוב") || bodyText.contains("סיכום");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * בודק שמופיעים כל הפרטים הנדרשים בתוצאות
     * @return true אם כל הפרטים מופיעים
     */
    public boolean verifyRequiredFields() {
        try {
            String resultsText = getResultsText();
            
            boolean dailyAverage = resultsText.contains("שכר יומי ממוצע") || 
                                  resultsText.contains("ממוצע יומי");
            boolean dailyUnemployment = resultsText.contains("דמי אבטלה ליום") || 
                                       resultsText.contains("אבטלה יומי");
            boolean monthlyUnemployment = resultsText.contains("דמי אבטלה לחודש") || 
                                         resultsText.contains("אבטלה חודשי");
            
            System.out.println("\nבדיקת שדות בתוצאות:");
            System.out.println("שכר יומי ממוצע: " + (dailyAverage ? "✅" : "❌"));
            System.out.println("דמי אבטלה ליום: " + (dailyUnemployment ? "✅" : "❌"));
            System.out.println("דמי אבטלה לחודש: " + (monthlyUnemployment ? "✅" : "❌"));
            
            return dailyAverage && dailyUnemployment && monthlyUnemployment;
            
        } catch (Exception e) {
            System.err.println("שגיאה בבדיקת שדות: " + e.getMessage());
            return false;
        }
    }

    /**
     * מחזיר את טקסט התוצאות
     */
    public String getResultsText() {
        try {
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
            return driver.findElement(By.tagName("body")).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
