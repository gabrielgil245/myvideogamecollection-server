package com.revature.myvideogamecollection.controller;

import com.revature.myvideogamecollection.model.JsonResponse;
import com.revature.myvideogamecollection.model.User;
import com.revature.myvideogamecollection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController("userController")
@RequestMapping(value="api")
@CrossOrigin(value=CrossOriginUtil.CROSS_ORIGIN_VALUE, allowCredentials = "true")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user")
    public JsonResponse getAllUsers() {
        JsonResponse response;
        try {
            response = new JsonResponse(true, "Listing all users", this.userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            response = new JsonResponse(false, "An error occurred", null);
        }
        return response;
    }

    @PostMapping("login")
    public JsonResponse login(HttpSession session, @RequestBody User user) {
        JsonResponse response;
        User existingUser = this.userService.getUserByUsername(user.getUsername());
        if(existingUser != null) { //NEEDS PASSWORD DECRYPTION
            session.setAttribute("userInSession", existingUser); //CONSIDER JWTs
            response = new JsonResponse(true, "Log in successful", existingUser);
        } else
            response = new JsonResponse(false, "Invalid Username or Password", null);
        return response;
    }

    @GetMapping("check-session")
    public JsonResponse checkSession(HttpSession session) {
        JsonResponse response;
        User currentUser = (User) session.getAttribute("userInSession");
        if(currentUser != null)
            response = new JsonResponse(true, "Session found", currentUser);
        else
            response = new JsonResponse(false, "Session not found", null);
        return response;
    }

    @GetMapping("logout")
    public JsonResponse logout(HttpSession session) {
        session.setAttribute("userInSession", null);
        return new JsonResponse(true, "Session terminated", null);
    }

    //NEEDS EMAIL DEPENDENCY & FUNCTIONALITY
    @GetMapping("forgot-password/{email}")
    public JsonResponse forgotPassword(@PathVariable String email) {
        JsonResponse response;
        User existingUser = this.userService.getUserByEmail(email);
        if(existingUser != null) {
            response = new JsonResponse(true, "Forgot Password Email Sent", existingUser);
        } else
            response = new JsonResponse(false, "Invalid Email", null);
        return response;
    }


    @PatchMapping("reset-password")
    public JsonResponse resetPassword(HttpSession session, @RequestBody User user) {
        JsonResponse response;
        User currentUser = this.userService.getUserByEmail(user.getEmail());

        if(currentUser == null)
            return new JsonResponse(false, "An error occurred in checking your email", null);

        String newPassword = user.getPassword();
        //ENCRYPT PASSWORD HERE
        currentUser.setPassword(newPassword);
        currentUser = this.userService.editUser(currentUser);
        if(currentUser != null) {
            //SEND SUCCESSFUL EMAIL RESET
            response = new JsonResponse(true, "Password was successfully updated", currentUser);
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
    public JsonResponse editUser(HttpSession session, @RequestBody User user) {
        JsonResponse response;
        User currentUser = (User) session.getAttribute("userInSession");
        if(currentUser != null) {
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setBirthday(user.getBirthday());
            currentUser.setAboutMe(user.getAboutMe());
            currentUser.setProfilePic(user.getProfilePic());

            if(this.userService.editUser(currentUser) != null)
                response = new JsonResponse(true, "Profile was successfully updated", currentUser);
            else
                response = new JsonResponse(false, "Error occurred during the profile update", null);
        } else
            response = new JsonResponse(false, "User not logged in", null);

        return response;
    }

    @DeleteMapping("user")
    public JsonResponse deleteUser(HttpSession session) {
        JsonResponse response;
        User currentUser = (User) session.getAttribute("userInSession");
        if(currentUser != null) {
            if(this.userService.deleteUser(currentUser)) {
                response = new JsonResponse(true, "Your account was successfully deleted", null);
                session.setAttribute("userInSession", null);
            } else
                response = new JsonResponse(false, "An error occurred", null);
        }
        else
            response = new JsonResponse(false, "User not logged in", null);
        return response;
    }
}
