/*
 * Copyright (c) 2016.
 * @gklimek
 */

package regexes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

    private final String PASSWORD_PATTERN =
            "^[!@#$%&*+=:;,.?<>A-Za-z0-9_-]{3,15}$";

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public boolean validate(final String text) {

        matcher = pattern.matcher(text);
        return matcher.matches();
    }
}