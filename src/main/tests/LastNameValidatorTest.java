package main.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import main.regexes.LastNameValidator;

public class LastNameValidatorTest {

    private LastNameValidator lastNameValidator;

    @BeforeClass
    public void initData(){
        lastNameValidator = new LastNameValidator();
    }

    @DataProvider
    public Object[][] ValidLastNameProvider() {
        return new Object[][]{
                {new String[] {
                        "Klimek", "Brzeczyszczykiewicz", "Li"
                }}
        };
    }

    @DataProvider
    public Object[][] InValidLastNameProvider() {
        return new Object[][]{
                {new String[] {
                        "K","Klimek1","dnfjnsdkjfnjsdkfksdnjfknsdjkfnkjssdhASDGGFDF", "klimek",
                        "Ham@", "KlimeK"
                }}
        };
    }

    @Test(dataProvider = "ValidLastNameProvider")
    public void ValidLastNameTest(String[] Username) {

        for(String temp : Username){
            boolean valid = lastNameValidator.validate(temp);
            System.out.println("Last name is valid : " + temp + " , " + valid);
            Assert.assertEquals(true, valid);
        }
    }

    @Test(dataProvider = "InValidLastNameProvider",
            dependsOnMethods= "ValidLastNameTest")
    public void InValidLastNameTest(String[] Username) {

        for(String temp : Username){
            boolean valid = lastNameValidator.validate(temp);
            System.out.println("Last name is valid : " + temp + " , " + valid);
            Assert.assertEquals(false, valid);
        }
    }
}