package com.example.barcodescannerdemo
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Test

class RegexUnitTest {

    @Test
    fun testPlusCodeRegex() {

        assertTrue(Constants.PLUS_CODE_FULL_REGEX.matches("7GJQ5J3V+9P"))
        assertTrue(Constants.PLUS_CODE_SHORT_REGEX.matches("P739+RV"))
        assertTrue(Constants.PLUS_CODE_SHORT_REGEX.matches("JFVW+8P"))
        assertTrue(Constants.PLUS_CODE_SHORT_REGEX.matches("Q33G+92P"))
        assertTrue(Constants.PLUS_CODE_SHORT_REGEX.matches("RJPW+CJF"))

    }

    @Test
    fun testLatitudeRegex() {

        assertTrue(Constants.LATITUDE_REGEX.matches("22.153388"))
        assertTrue(Constants.LATITUDE_REGEX.matches("51.707130"))
        assertTrue(Constants.LATITUDE_REGEX.matches("51.643246"))
        assertTrue(Constants.LATITUDE_REGEX.matches("-30.403698"))
        assertTrue(Constants.LATITUDE_REGEX.matches("65.746337"))

    }

    @Test
    fun testLongitudeRegex() {

        assertTrue(Constants.LONGITUDE_REGEX.matches("35.644312"))
        assertTrue(Constants.LONGITUDE_REGEX.matches("5.269945"))
        assertTrue(Constants.LONGITUDE_REGEX.matches("5.496812"))
        assertTrue(Constants.LONGITUDE_REGEX.matches("19.998783"))
        assertTrue(Constants.LONGITUDE_REGEX.matches("-168.936530"))

    }

    @Test
    fun testSixNumbersRegex() {

        assertTrue(Constants.SIX_NUMBERS_REGEX.matches("000000"))
        assertTrue(Constants.SIX_NUMBERS_REGEX.matches("284056"))
        assertTrue(Constants.SIX_NUMBERS_REGEX.matches("184052"))
        assertTrue(Constants.SIX_NUMBERS_REGEX.matches("765123"))
        assertTrue(Constants.SIX_NUMBERS_REGEX.matches("909090"))

    }

    @Test
    fun testAnyNumbersRegex() {

        assertTrue(Constants.NUMBERS_REGEX.matches("123456789"))
        assertTrue(Constants.NUMBERS_REGEX.matches("9876543210"))
        assertTrue(Constants.NUMBERS_REGEX.matches("2468"))
        assertTrue(Constants.NUMBERS_REGEX.matches("13579"))
        assertTrue(Constants.NUMBERS_REGEX.matches("777777"))

    }



}