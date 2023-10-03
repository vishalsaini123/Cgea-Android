package com.creditgaea.sample.credit.java.creditgea.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValidationUtils {

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static final String PASSWORD_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    // static Pattern object, since pattern is fixed
    private static Pattern pattern;
    // non-static Matcher object because it's created from the input String

    final static String ONE_DIGIT = "^(?=.*[0-9])";  //(?=.*[0-9]) a digit must occur at least once
    final static String LOWER_CASE = "(?=.*[a-z])";  //(?=.*[a-z]) a lower case letter must occur at least once
    final static String MIN_LENGHT=".{8,}$";
    final static String NO_SPACE = "(?=\\S+$)";
    private Matcher matcher;


    public static boolean isValidEmail(String email) {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static   boolean isValidPassword(String password){
        String PATTERN = ONE_DIGIT + LOWER_CASE + NO_SPACE + MIN_LENGHT;
       return password.matches(PATTERN);

    }
}