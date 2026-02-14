package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * דף אבטלה - מייצג את הדף הראשי של אבטלה באתר ביטוח לאומי
 */
public class UnemploymentPage extends BtlBasePage {

    public UnemploymentPage(WebDriver driver) {
        super(driver);
    }

    /**
     * מחזיר את כותרת הדף
     */
    public String getPageTitle() {
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h1"))
        );
        return title.getText();
    }

    /**
     * לוחץ על הלינק "מחשבוני דמי אבטלה"
     * משתמש ב-XPath גמיש (מתוך הקורס עמ' 46-47)
     */
    public void clickOnCalculatorsLink() {
        try {
            // ניסיון ראשון - partialLinkText
            WebElement calculatorsLink = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.partialLinkText("מחשבוני דמי אבטלה")
                    )
            );
            calculatorsLink.click();
        } catch (Exception e) {
            // ניסיון שני - XPath שמחפש לינקים עם המילה "מחשבון"
            WebElement calculatorsLink = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//a[contains(text(),'מחשבון') and contains(text(),'אבטלה')]")
                    )
            );
            calculatorsLink.click();
        }
    }

    /**
     * לוחץ על הלינק "חישוב סכום דמי אבטלה"
     * משתמש בטכניקות מתקדמות מהקורס (CSS + XPath)
     */
    public UnemploymentCalculatorPage clickOnAmountCalculator() {
        try {
            // ניסיון ראשון - partialLinkText
            WebElement amountCalculatorLink = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.partialLinkText("חישוב סכום דמי אבטלה")
                    )
            );
            amountCalculatorLink.click();
        } catch (Exception e) {
            try {
                // ניסיון שני - XPath גמיש יותר
                WebElement amountCalculatorLink = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//a[contains(text(),'חישוב') and contains(text(),'סכום')]")
                        )
                );
                amountCalculatorLink.click();
            } catch (Exception e2) {
                // ניסיון שלישי - CSS Selector לפי href
                WebElement amountCalculatorLink = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector("a[href*='Calculator'], a[href*='Simulator']")
                        )
                );
                amountCalculatorLink.click();
            }
        }
        return new UnemploymentCalculatorPage(driver);
    }
}
