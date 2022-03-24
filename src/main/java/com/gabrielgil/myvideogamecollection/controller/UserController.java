package com.gabrielgil.myvideogamecollection.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielgil.myvideogamecollection.model.JsonResponse;
import com.gabrielgil.myvideogamecollection.model.User;
import com.gabrielgil.myvideogamecollection.service.UserService;
import com.gabrielgil.myvideogamecollection.utility.SharedUtil;
import com.gabrielgil.myvideogamecollection.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController("userController")
@RequestMapping(value = "api")
@CrossOrigin(value = SharedUtil.CROSS_ORIGIN_VALUE, allowCredentials = "true")
public class UserController {
    private UserService userService;
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("user")
    public JsonResponse getAllUsers() {
        JsonResponse response;
        try {
            List<User> users = this.userService.getAllUsers();
            for(User user: users)
                user.setPassword(null);
            response = new JsonResponse(true, "Listing all users", users);
        } catch (Exception exception) {
            exception.printStackTrace();
            response = new JsonResponse(false, "An error occurred", null);
        }
        return response;
    }

    @PostMapping("login")
    public JsonResponse login(HttpSession session, @RequestBody User user) {
        JsonResponse response;
        User existingUser = this.userService.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(existingUser != null) {
            String token = jwtUtil.generateToken(existingUser.getUserId());
            existingUser.setPassword(null);
            response = new JsonResponse(true, token, existingUser);
        } else
            response = new JsonResponse(false, "Invalid Username or Password", null);
        return response;
    }

    //NEEDS EMAIL DEPENDENCY & FUNCTIONALITY
    @GetMapping("forgot-password/{email}")
    public JsonResponse forgotPassword(@PathVariable String email) {
        JsonResponse response;
        User existingUser = this.userService.getUserByEmail(email);
        if(existingUser != null) {
            //SEND FORGOT PASSWORD EMAIL HERE - IDEA SEND EMAIL WITH EMAIL & TOKEN PATH VARIABLES
            response = new JsonResponse(true, "Forgot Password Email Sent", null);
        } else
            response = new JsonResponse(false, "Invalid Email", null);
        return response;
    }

    @PatchMapping("reset-password") // To later change to "reset-password/{password} + Headers JWT
    public JsonResponse resetPassword(@RequestBody User user) {
        JsonResponse response;
        User currentUser = this.userService.getUserByEmail(user.getEmail());
        if(currentUser == null)
            return new JsonResponse(false, "An error occurred in checking your email", null);
        String newPassword = user.getPassword();
        //ENCRYPT PASSWORD HERE
        currentUser.setPassword(newPassword);
        currentUser = this.userService.editUser(currentUser);
        if(currentUser != null) {
            String token = jwtUtil.generateToken(currentUser.getUserId());
            currentUser.setPassword(null);
            response = new JsonResponse(true, token, currentUser);
        } else
            response = new JsonResponse(false, "An error occurred during the password change", null);

        return response;
    }

    @PostMapping("user")
    public JsonResponse createUser(@RequestBody User user) {
        JsonResponse response;
        //SET PASSWORD ENCRYPTION HERE
        User newUser = this.userService.createUser(user);
        if(newUser != null)
            response = new JsonResponse(true, "User successfully created", newUser);
        else
            response = new JsonResponse(false, "Username/Email entered already exists", null);
        return response;
    }

    @PatchMapping("user")
    public JsonResponse editUser(@RequestBody User user, @RequestHeader Map<String, String> headers) {
        JsonResponse response;
        DecodedJWT decodedJWT = jwtUtil.verify(headers.get("authorization"));
        if(decodedJWT == null)
            response = new JsonResponse(false, "Invalid token, no authorization", null);
        else {
            if(decodedJWT.getClaims().get("userId").asInt() == user.getUserId()) {
                // Password encryption goes here
                User updatedUser = this.userService.editUser(user);
                updatedUser.setPassword(null); // Prevent sensitive information from getting leaked
                if(updatedUser == null)
                    response = new JsonResponse(false, "Invalid token, user does not exist", null);
                else
                    response = new JsonResponse(true, "Valid token, user updated", updatedUser);
            } else
                response = new JsonResponse(false, "Invalid token, user mismatch", null);
        }
        return response;
    }

    @DeleteMapping("user/{userId}")
    public JsonResponse deleteUser(@PathVariable Integer userId, @RequestHeader Map<String, String> headers) {
        JsonResponse response;
        DecodedJWT decodedJWT = jwtUtil.verify(headers.get("authorization"));
        if(decodedJWT == null)
            response = new JsonResponse(false, "Invalid token, no authorization", null);
        else {
            if(decodedJWT.getClaims().get("userId").asInt() == userId) {
                User user = userService.findUserById(userId);
                if(user != null) {
                    Boolean deleted = userService.deleteUser(user);
                    if(deleted == true)
                        response = new JsonResponse(true, "Valid token, user successfully deleted", null);
                    else
                        response = new JsonResponse(false, "User was not successfully deleted", null);
                } else
                    response = new JsonResponse(false, "Invalid token, user does not exist", null);
            }
            else
                response = new JsonResponse(false, "Invalid token, user mismatch", null);
        }
        return response;
    }
}
