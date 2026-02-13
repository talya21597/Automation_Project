package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BtlBasePage extends BasePage {

    @FindBy(id = "divBreadcrumbs")
    private WebElement breadcrumbsBar;

    @FindBy(id = "TopQuestions")
    private WebElement searchField;

    @FindBy(linkText = "סניפים")
    private WebElement branchesBtn;

    public BtlBasePage(WebDriver driver) {
        super(driver);
        // השורה הזו היא ה"קסם" שמפעיל את ה-@FindBy
        PageFactory.initElements(driver, this);
    }

    public void clickMainMenu(MainMenu menu) {
        String menuText = "";
        switch (menu) {
            case KITZBAOT_VE_HATAVOT: menuText = "קצבאות והטבות"; break;
            case MEYDA_VE_ZCHUYOT:    menuText = "מיצוי זכויות"; break;
            case SHIRUT_ISHI:         menuText = "שירות אישי"; break;
            case TASHUMIM:            menuText = "תשלומים"; break;
            case Odot:               menuText = "אודות"; break;
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(menuText))).click();
    }

    public String getBreadcrumbsText() {
        wait.until(ExpectedConditions.visibilityOf(breadcrumbsBar));
        return breadcrumbsBar.getText();
    }
}