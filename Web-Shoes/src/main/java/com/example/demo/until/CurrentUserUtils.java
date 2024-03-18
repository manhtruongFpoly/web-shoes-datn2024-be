package com.example.demo.until;

import com.example.demo.security.CustomerDetailService;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtils {

    public static CustomerDetailService getCurrentUserUtils(){
        return (CustomerDetailService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
