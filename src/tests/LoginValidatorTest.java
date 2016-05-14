package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import regexes.LoginValidator;

public class LoginValidatorTest {

    private LoginValidator loginValidator;

    @BeforeClass
    public void initData(){
        loginValidator = new LoginValidator();
    }

    @DataProvider
    public Object[][] ValidLoginProvider() {
        return new Object[][]{
                {new String[] {
                        "mkyong34", "mkyong_2002","mkyong2002" ,"mk34_yong", "_grzegorz_"
                }}
        };
    }

    @DataProvider
    public Object[][] InvalidLoginProvider() {
        return new Object[][]{
                {new String[] {
                        "mk","mk@yong","mkyong-123456789", "jajestem_misztrz12"
                }}
        };
    }

    @Test(dataProvider = "ValidLoginProvider")
    public void ValidLoginTest(String[] Username) {

        for(String temp : Username){
            boolean valid = loginValidator.validate(temp);
            System.out.println("Login is valid : " + temp + " , " + valid);
            Assert.assertEquals(true, valid);
        }

    }

    @Test(dataProvider = "InvalidLoginProvider",
            dependsOnMethods="ValidLoginTest")
    public void InValidLoginTest(String[] Username) {

        for(String temp : Username){
            boolean valid = loginValidator.validate(temp);
            System.out.println("Login is valid : " + temp + " , " + valid);
            Assert.assertEquals(false, valid);
        }
    }
}