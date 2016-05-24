/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.regexes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstNameValidator {

    private Pattern pattern;
    private Matcher matcher;

    private final String FIRSTNAME_PATTERN =
            "^[A-Z]([a-z\\+]){1,36}$";

    public FirstNameValidator() {
        pattern = Pattern.compile(FIRSTNAME_PATTERN);
    }

    public boolean validate(final String text) {

        matcher = pattern.matcher(text);
        return matcher.matches();
    }
}