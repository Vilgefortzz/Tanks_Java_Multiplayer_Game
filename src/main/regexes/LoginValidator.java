/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.regexes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidator {

    private Pattern pattern;
    private Matcher matcher;

    private final String LOGIN_PATTERN = "^[a-z0-9_]{3,15}$";

    public LoginValidator(){
        pattern = Pattern.compile(LOGIN_PATTERN);
    }

    public boolean validate(final String login){

        matcher = pattern.matcher(login);
        return matcher.matches();
    }
}