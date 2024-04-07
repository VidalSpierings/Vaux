package com.example.barcodescannerdemo

object Constants {
    val PLUS_CODE_FULL_REGEX = "^[2-9C][2-9CFGHJMPQRV](0{6}\\+|[2-9CFGHJMPQRVWX]{2}(0000\\+|[2-9CFGHJMPQRVWX]{2}(00\\+|[2-9CFGHJMPQRVWX]{2}\\+([2-9CFGHJMPQRVWX]{2,7})?)))\$".toRegex()
    val PLUS_CODE_SHORT_REGEX = "^([2-9CFGHJMPQRVWX]{2})?([2-9CFGHJMPQRVWX]{2})?[2-9CFGHJMPQRVWX]{2}\\+([2-9CFGHJMPQRVWX]{2,7})?\$".toRegex()
    val LATITUDE_REGEX = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)\$".toRegex()
    val LONGITUDE_REGEX = "[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)\$".toRegex()
    val SIX_NUMBERS_REGEX = "\\d{6}".toRegex()
    val NUMBERS_REGEX = "^\\d+$".toRegex()
}