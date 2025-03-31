package com.uni.bankproject.exception;

import com.uni.bankproject.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleRuntimeException(RuntimeException ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("error");
        model.addAttribute("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("error");
        model.addAttribute("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleCustomAuthenticationException(HttpServletResponse response, Model model, CustomAuthenticationException ex) {
        Cookie cookie = CookieUtils.createCookie("jwt", "", 0);
        response.addCookie(cookie);
        response.addHeader("Set-Cookie", CookieUtils.addCookie(cookie, "Strict"));
        ModelAndView modelAndView = new ModelAndView("redirect:/api/v1/auth/login");
        model.addAttribute("errorMessage", ex.getMessage());
        return modelAndView;
    }
}