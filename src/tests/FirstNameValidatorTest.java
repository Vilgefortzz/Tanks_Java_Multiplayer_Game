package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import regexes.FirstNameValidator;

public class FirstNameValidatorTest {

    private FirstNameValidator firstNameValidator;

    @BeforeClass
    public void initData(){
        firstNameValidator = new FirstNameValidator();
    }

    @DataProvider
    public Object[][] ValidFirstNameProvider() {
        return new Object[][]{
                {new String[] {
                        "Grzegorz", "Halina", "Szczepan", "Yo"
                }}
        };
    }

    @DataProvider
    public Object[][] InValidFirstNameProvider() {
        return new Object[][]{
                {new String[] {
                        "G","Grzegorz12","dnfjnsdkjfnjsdkfksdnjfknsdjkfnkjssdhASDGGFDF", "grzegorz",
                        "Wojtek@", "GRzegorz", "GR"
                }}
        };
    }

    @Test(dataProvider = "ValidFirstNameProvider")
    public void ValidFirstNameTest(String[] Username) {

        for(String temp : Username){
            boolean valid = firstNameValidator.validate(temp);
            System.out.println("First name is valid : " + temp + " , " + valid);
            Assert.assertEquals(true, valid);
        }
    }

    @Test(dataProvider = "InValidFirstNameProvider",
            dependsOnMethods= "ValidFirstNameTest")
    public void InValidFirstNameTest(String[] Username) {

        for(String temp : Username){
            boolean valid = firstNameValidator.validate(temp);
            System.out.println("First name is valid : " + temp + " , " + valid);
            Assert.assertEquals(false, valid);
        }
    }
}