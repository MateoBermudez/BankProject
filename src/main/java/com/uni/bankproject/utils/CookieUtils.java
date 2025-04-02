package com.uni.bankproject.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtils {

    public static Cookie getCookie(Cookie[] cookies, String name) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static String getCookieValue(Cookie[] cookies, String name) {
        Cookie cookie = getCookie(cookies, name);
        return cookie == null ? null : cookie.getValue();
    }

    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static String addCookie(Cookie cookie, String sameSite) {
        return String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly; SameSite=%s",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(), cookie.getPath(), sameSite);
    }
}
