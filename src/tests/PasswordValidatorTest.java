package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import regexes.PasswordValidator;

public class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeClass
    public void initData(){
        passwordValidator = new PasswordValidator();
    }

    @DataProvider
    public Object[][] ValidPasswordProvider() {
        return new Object[][]{
                {new String[] {
                        "klepsydra", "Witam@189.","Spra$wdzamy!"
                }}
        };
    }

    @DataProvider
    public Object[][] InValidPasswordProvider() {
        return new Object[][]{
                {new String[] {
                        "ju","1%","dsfnsdjkfksdfnQWE", ".Sprawdzam%)"
                }}
        };
    }

    @Test(dataProvider = "ValidPasswordProvider")
    public void ValidPasswordTest(String[] Username) {

        for(String temp : Username){
            boolean valid = passwordValidator.validate(temp);
            System.out.println("Password is valid : " + temp + " , " + valid);
            Assert.assertEquals(true, valid);
        }

    }

    @Test(dataProvider = "InValidPasswordProvider",
            dependsOnMethods="ValidPasswordTest")
    public void InValidPasswordTest(String[] Username) {

        for(String temp : Username){
            boolean valid = passwordValidator.validate(temp);
            System.out.println("Password is valid : " + temp + " , " + valid);
            Assert.assertEquals(false, valid);
        }
    }
}
