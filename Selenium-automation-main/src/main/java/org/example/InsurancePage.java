package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InsurancePage extends BtlBasePage {

    public InsurancePage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitleText() {
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h1"))
        );
        return title.getText();
    }

    public InsuranceCalculatorPage clickOnCalculatorLink() {
        WebElement calculatorLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.partialLinkText("מחשבון"))
        );
        calculatorLink.click();
        return new InsuranceCalculatorPage(driver);
    }
}
