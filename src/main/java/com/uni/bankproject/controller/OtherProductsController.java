package com.uni.bankproject.controller;

import com.uni.bankproject.entity.OtherProducts;
import com.uni.bankproject.service.OtherProductsService;
import com.uni.bankproject.utils.CookieUtils;
import com.uni.bankproject.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class OtherProductsController {

    private final OtherProductsService otherProductsService;
    private final JwtUtils jwtUtils;

    @Autowired
    public OtherProductsController(OtherProductsService otherProductsService, JwtUtils jwtUtils) {
        this.otherProductsService = otherProductsService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/createOtherProduct")
    public String createOtherProduct(Model model, HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userid = otherProductsService.getIdByUsername(username);
            model.addAttribute("titulo", "Crear otro producto");
            model.addAttribute("Userid", userid);
            OtherProducts otherProduct = new OtherProducts();
            otherProduct.setUserId(userid);
            otherProduct.setProductid(otherProductsService.generateUniqueProductNumber());
            model.addAttribute("otherProduct", otherProduct);
            return "createOtherProduct";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/createOtherProduct")
    public String createOtherProductPost(Model model, HttpServletRequest request, OtherProducts otherProduct) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userid = otherProductsService.getIdByUsername(username);
            otherProduct.setUserId(userid);
            otherProduct.setCreateAt(new Date());
            otherProduct.setProductid(otherProductsService.generateUniqueProductNumber());
            String message = otherProductsService.createProduct(otherProduct);
            model.addAttribute("message", message);
            return "createOtherProduct";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/getOtherProducts")
    public String getOtherProducts(Model model, HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userid = otherProductsService.getIdByUsername(username);
            model.addAttribute("titulo", "Listado de otros productos");
            model.addAttribute("Userid", userid);
            model.addAttribute("otherProducts", otherProductsService.getProductsById(userid));
            return "getOtherProducts";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/deleteOtherProduct{productid}")
    public String deleteOtherProduct(@PathVariable String productid, Model model) {
        model.addAttribute("titulo", "Eliminar producto");
        String message = otherProductsService.deleteProduct(productid);
        model.addAttribute("message", message);
        return "deleteOtherProduct";
    }

}
