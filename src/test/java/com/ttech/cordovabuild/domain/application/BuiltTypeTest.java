package com.ttech.cordovabuild.domain.application;


import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 10/12/13
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */

public class BuiltTypeTest {

    @Test
    public void testBuildType() {
        assertEquals(BuiltType.ANDROID, BuiltType.getValueOfIgnoreCase("android"));
        assertEquals(BuiltType.ANDROID, BuiltType.getValueOfIgnoreCase("anDroid"));
        assertEquals(BuiltType.IOS, BuiltType.getValueOfIgnoreCase("IoS"));
    }
}
