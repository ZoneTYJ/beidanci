package com.example.zahnubeidanci.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {
	
	public static String unescapeUnicode(String str){
        StringBuffer b=new StringBuffer();
        Matcher m = Pattern.compile("\\\\u([0-9a-fA-F]{4})").matcher(str);
        while(m.find())
            b.append((char)Integer.parseInt(m.group(1),16));
        return b.toString();
    }
}
