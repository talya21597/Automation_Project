package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BranchDetailsPage extends BtlBasePage {

    public BranchDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAllRequiredInfoDisplayed() {
        try {
            // המתנה לטעינת הגוף של הדף החדש
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));
            Thread.sleep(2000); // המתנה קצרה לייצוב הדף

            String pageText = driver.findElement(By.tagName("body")).getText();

            // הדפסה ללוג כדי שתראי איפה הוא נמצא
            System.out.println("--- טקסט שנמצא בדף הנוכחי (דגימה) ---");
            System.out.println(pageText.substring(0, Math.min(pageText.length(), 200)) + "...");
            System.out.println("----------------------------------------");

            boolean address = pageText.contains("כתובת");
            boolean reception = pageText.contains("קבלת קהל");
            boolean phone = pageText.contains("מענה טלפוני") || pageText.contains("מוקד");

            return address && reception && phone;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}