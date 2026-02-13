package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait; // ייבוא חסר
import java.time.Duration; // ייבוא עבור הזמן

/**
 * מחלקה אבסטרקטית המהווה בסיס לכל מחלקות הדפים באתר ביטוח לאומי.
 */
public abstract class BasePage {

    protected WebDriver driver;
    // הוספת משתנה wait מסוג protected כדי שכל הדפים יוכלו להשתמש בו
    protected WebDriverWait wait;

    /**
     * @param driver הדרייבר שנוצר בטסט
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;

        // אתחול ה-wait עם המתנה של 10 שניות (דרישה מהמשימה שלך)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // אתחול האלמנטים בדף
        PageFactory.initElements(driver, this);
    }
}