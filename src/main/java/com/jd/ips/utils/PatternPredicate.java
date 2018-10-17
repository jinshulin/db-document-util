package com.jd.ips.utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @description  
 *
 * @author jinshulin(jinshulin@jd.com)
 * @since 2018年10月15日 18时49分
 */
public class PatternPredicate implements Predicate<String> {

    private Pattern p;

    public PatternPredicate(String pattern) {
        p = Pattern.compile(pattern);
    }

    @Override
    public boolean test(String s) {
        return p.matcher(s).find();
    }
}
