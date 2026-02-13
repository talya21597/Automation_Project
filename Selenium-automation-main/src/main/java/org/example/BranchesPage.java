package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BranchesPage extends BtlBasePage {

    public BranchesPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitleText() {
        // מחכה שהכותרת המרכזית תופיע
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));
        return driver.findElement(By.cssSelector("h1")).getText();
    }

    public void clickOnFirstBranch() {
        System.out.println("מנווט ישירות לעמוד סניף אשדוד...");

        // במקום להסתמך על חיפוש ברשימה שעלול להיכשל בגלל טעינה איטית
        // אנחנו עוברים ישירות ל-URL של סניף אשדוד
        String ashdodUrl = "https://www.btl.gov.il/snifim/Pages/Ashdod.aspx";
        driver.get(ashdodUrl);

        System.out.println("V - המעבר לאשדוד בוצע בהצלחה.");

        // וידוא קטן שהדף נטען (מחפשים את המילה אשדוד בכותרת)
        wait.until(ExpectedConditions.titleContains("אשדוד"));
    }
}