package com.croumy.hltbwearos

class Helper {
    companion object {
        fun getCookieValueByKey(cookieString: String, searchedKey: String): String {
            var value = "";
            val cookiesValues = cookieString.split("; ")
            cookiesValues.forEach { cookie ->
                val keyValue = cookie.split("=")
                if (keyValue.size == 2) {
                    val cookieKey = keyValue[0]
                    if (cookieKey == searchedKey) {
                        value = keyValue[1]
                    }
                }
            }
            return value
        }
    }
}