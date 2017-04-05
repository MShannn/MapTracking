package com.snow.map.tracking.Validator;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Muhammad Shan on 27/12/2016.
 */

public class Validations {

    static double  twoDigits;
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    public  static double roundTwoDecimal(double value ){
        DecimalFormat df = new DecimalFormat("#.####");
        twoDigits = Double.valueOf(df.format(value));
        return  twoDigits;
    }
}
