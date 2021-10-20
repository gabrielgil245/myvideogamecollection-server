package com.revature.myvideogamecollection.controller;

import com.revature.myvideogamecollection.model.Game;
import com.revature.myvideogamecollection.model.JsonResponse;
import com.revature.myvideogamecollection.model.Platform;
import com.revature.myvideogamecollection.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("gameController")
@RequestMapping(value="api")
@CrossOrigin(value=CrossOriginUtil.CROSS_ORIGIN_VALUE, allowCredentials = "true")
public class GameController {
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("game")
    public JsonResponse getAllGames() {
        JsonResponse response;
        List<Game> allGames = this.gameService.getAllGames();
        if(allGames != null)
            response = new JsonResponse(true, "Listing all games", allGames);
        else
            response = new JsonResponse(false, "An error occurred", null);
        return response;
    }

    @GetMapping("game/{userId}")
    public JsonResponse getGamesByUserId(@PathVariable Integer userId) {
        JsonResponse response;
        List<Game> userGames;
        userGames = this.gameService.getGamesByUserId(userId);
        if(userGames != null) {
            if(userGames.size() > 0)
                response = new JsonResponse(true, "Listing user's games", userGames);
            else
                response = new JsonResponse(false, "No games found for this user", null);
        } else
            response = new JsonResponse(false, "User does not exist", null);
        return response;
    }

    @GetMapping("game/{userId}/{platformId}")
    public JsonResponse getGamesByUserIdAndPlatformId(@PathVariable Integer userId, @PathVariable Integer platformId) {
        JsonResponse response;
        List<Game> userGames;
        userGames = this.gameService.getGamesByPlatformId(userId, platformId);
        if(userGames != null) {
            if(userGames.size() > 0)
                response = new JsonResponse(true, "Listing games by user and platform", userGames);
            else
                response = new JsonResponse(false, "No games found for this user", null);
        } else
            response = new JsonResponse(false, "User or Platform does not exist", null);
        return response;
    }

    @PostMapping("game")
    public JsonResponse createGame(@RequestBody Game game) {
        JsonResponse response;
        Game newGame = this.gameService.createGame(game);
        if(newGame != null)
            response = new JsonResponse(true, "Game successfully added", newGame);
        else
            response = new JsonResponse(false, "Was unable to add the game to your account", null);
        return response;
    }

    @PatchMapping("game")
    public JsonResponse editGame(@RequestBody Game game) {
        JsonResponse response;
        Game alteredGame = this.gameService.editGame(game);
        if(alteredGame != null)
            response = new JsonResponse(true, "Game successfully altered", alteredGame);
        else
            response = new JsonResponse(false, "Was unable to edit the selected Game", null);
        return response;
    }

    @DeleteMapping("game/{gameId}")
    public JsonResponse deleteGame(@PathVariable Integer gameId) {
        JsonResponse response;
        Boolean success = this.gameService.deleteGame(gameId);
        if(success)
            response = new JsonResponse(true, "Game was successfully deleted", null);
        else
            response = new JsonResponse(false, "Was unable to delete the game from your account", null);
        return response;
    }

}
