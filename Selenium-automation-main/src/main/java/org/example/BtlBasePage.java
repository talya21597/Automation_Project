package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BtlBasePage extends BasePage {

    // שימוש ב-CSS Selector גמיש יותר - מחפש את ה-breadcrumbs לפי class או id
    @FindBy(css = "#divBreadcrumbs, .breadcrumbs, [class*='breadcrumb'], nav[aria-label*='breadcrumb']")
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
            case DMEY_BITUACH:        menuText = "דמי ביטוח"; break;
            case Odot:                menuText = "אודות"; break;
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(menuText))).click();
    }

    public void clickSubMenu(String subMenuText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(subMenuText))).click();
    }

    public String getBreadcrumbsText() {
        try {
            // ניסיון ראשון - לפי @FindBy
            wait.until(ExpectedConditions.visibilityOf(breadcrumbsBar));
            return breadcrumbsBar.getText();
        } catch (Exception e) {
            // ניסיון שני - XPath גמיש (מתוך הקורס עמ' 46-47)
            try {
                WebElement breadcrumb = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//nav[contains(@class,'breadcrumb')] | //div[contains(@class,'breadcrumb')] | //ol[contains(@class,'breadcrumb')]")
                ));
                return breadcrumb.getText();
            } catch (Exception e2) {
                // ניסיון שלישי - מחפש כל אלמנט שמכיל את המילה דף הבית
                WebElement breadcrumb = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'דף הבית') or contains(text(),'עמוד הבית')]")
                ));
                return breadcrumb.getText();
            }
        }
    }

    public void search(String searchText) {
        wait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();
        searchField.sendKeys(searchText);
        searchField.sendKeys(Keys.ENTER);
    }

    public BranchesPage clickBranches() {
        wait.until(ExpectedConditions.elementToBeClickable(branchesBtn)).click();
        return new BranchesPage(driver);
    }
}