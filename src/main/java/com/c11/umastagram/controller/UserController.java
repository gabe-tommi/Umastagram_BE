/*
    Author: Armando Vega
    Date Created: 18 November 2025
    Last Modified By: Armando Vega
    Date Last Modified: 18 November 2025
    Description: UserController class to handle user-related endpoints.
*/
package com.c11.umastagram.controller;

import com.c11.umastagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    // Define user-related endpoints here
    

}