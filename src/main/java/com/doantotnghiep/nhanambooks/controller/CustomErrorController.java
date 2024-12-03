package com.doantotnghiep.nhanambooks.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, @RequestParam(required = false) String code){
        String errorPage = "error/error";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                errorPage = "error/404";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 403 Forbidden error
                errorPage = "error/403";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                errorPage = "error/500";
            }
        }
        if(!StringUtils.isEmpty(code)){
            if("DC".equalsIgnoreCase(code)){
                errorPage = "error/discount";
            }
            if("not-found".equalsIgnoreCase(code)){
                errorPage = "error/not-found";
            }
            if("PQ".equalsIgnoreCase(code)){
                errorPage = "error/product-quantity";
            }

        }
        return errorPage;
    }


    public String getErrorPath(){
        return "/error";
    }


}
