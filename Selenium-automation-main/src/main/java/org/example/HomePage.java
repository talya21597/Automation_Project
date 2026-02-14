package org.example;

import org.openqa.selenium.WebDriver;

public class HomePage extends BtlBasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public InsurancePage navigateToInsurance() {
        clickMainMenu(MainMenu.DMEY_BITUACH);
        clickSubMenu("דמי ביטוח לאומי");
        return new InsurancePage(driver);
    }
    
    public UnemploymentPage navigateToUnemployment() {
        clickMainMenu(MainMenu.KITZBAOT_VE_HATAVOT);
        clickSubMenu("אבטלה");
        return new UnemploymentPage(driver);
    }
}