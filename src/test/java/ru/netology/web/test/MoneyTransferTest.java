package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @Test
    void shouldTransferFrom1to2() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        //Read balance and set transfer amount
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var transfer = DataHelper.getValidAmount(firstCardBalance);

        //Set expected
        var expectedFirstCard = firstCardBalance - transfer;
        var expectedSecondCard = secondCardBalance + transfer;

        //Make transfer
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(transfer), firstCardInfo);

        var actualFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedFirstCard, actualFirstCard);
        assertEquals(expectedSecondCard, actualSecondCard);
    }

    @Test
    void shouldTransferFrom2to1() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        //Read balance and set transfer amount
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var transfer = DataHelper.getValidAmount(firstCardBalance);

        //Set expected
        var expectedFirstCard = firstCardBalance + transfer;
        var expectedSecondCard = secondCardBalance - transfer;

        //Make transfer
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(transfer), secondCardInfo);

        var actualFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedFirstCard, actualFirstCard);
        assertEquals(expectedSecondCard, actualSecondCard);
    }

    //Bug found - reported
    /*
    @Test
    void shouldNotTransferFrom1to2() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        //Read balance and set transfer amount
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var transfer = DataHelper.getInvalidAmount(firstCardBalance);

        //Make transfer
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        transferPage.makeTransfer(String.valueOf(transfer), firstCardInfo);
        transferPage.findErrorMessage("Ошибка! ");
        var actualFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(firstCardBalance, actualFirstCard);
        assertEquals(secondCardBalance, actualSecondCard);
    }
    */
}
