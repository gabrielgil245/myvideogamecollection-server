package com.gabrielgil.myvideogamecollection.controller;

import com.gabrielgil.myvideogamecollection.model.JsonResponse;
import com.gabrielgil.myvideogamecollection.model.Platform;
import com.gabrielgil.myvideogamecollection.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("platformController")
@RequestMapping(value="api")
@CrossOrigin(value=CrossOriginUtil.CROSS_ORIGIN_VALUE, allowCredentials = "true")
public class PlatformController {
    PlatformService platformService;

    @Autowired
    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("platform")
    public JsonResponse getAllPlatforms() {
        JsonResponse response;
        List<Platform> allPlatforms = this.platformService.getAllPlatforms();
        if(allPlatforms != null)
            response = new JsonResponse(true, "Listing all platforms", allPlatforms);
        else
            response = new JsonResponse(false, "An error occurred", null);
        return response;
    }

    @GetMapping("platform/{userId}")
    public JsonResponse getPlatformsByUserId(@PathVariable Integer userId) {
        JsonResponse response;
        List<Platform> userPlatforms;
        userPlatforms = this.platformService.getPlatformsByUserId(userId);
        if(userPlatforms != null) {
            if(userPlatforms.size() > 0)
                response = new JsonResponse(true, "Listing user's platforms", userPlatforms);
            else
                response = new JsonResponse(false, "No platforms found for this user", null);
        } else
            response = new JsonResponse(false, "User does not exist", null);
        return response;
    }

    @PostMapping("platform")
    public JsonResponse createPlatform(@RequestBody Platform platform) {
        JsonResponse response;
        Platform newPlatform = this.platformService.createPlatform(platform);
        if(newPlatform != null)
            response = new JsonResponse(true, "Platform successfully added", newPlatform);
        else
            response = new JsonResponse(false, "Was unable to add the platform to your account", null);
        return response;
    }

    @PatchMapping("platform")
    public JsonResponse editPlatform(@RequestBody Platform platform) {
        JsonResponse response;
        Platform alteredPlatform = this.platformService.editPlatform(platform);
        if(alteredPlatform != null)
            response = new JsonResponse(true, "Platform successfully altered", alteredPlatform);
        else
            response = new JsonResponse(false, "Was unable to edit the selected platform", null);
        return response;
    }

    @DeleteMapping("platform/{platformId}")
    public JsonResponse deletePlatform(@PathVariable Integer platformId) {
        JsonResponse response;
        Boolean success = this.platformService.deletePlatform(platformId);
        if(success)
            response = new JsonResponse(true, "Platform was successfully deleted", null);
        else
            response = new JsonResponse(false, "Was unable to delete the platform from your account", null);
        return response;
    }

}
