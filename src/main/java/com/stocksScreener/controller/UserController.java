package com.stocksScreener.controller;

import com.stocksScreener.model.User;
import com.stocksScreener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getUser(HttpServletRequest httpServletRequest) {
        String jwtEmailId = (String) httpServletRequest.getAttribute("emailId");
        User user = userService.findByUserEmail(jwtEmailId).orElse(null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/profile/watchlist/add")
    public ResponseEntity<?> addSymbols(@RequestBody Map<String, List<String>> body,
                                        HttpServletRequest httpServletRequest) {
        String jwtEmailId = (String) httpServletRequest.getAttribute("emailId");
        List<String> symbols = body.get("symbols");
        if(symbols == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            userService.addToWatchList(jwtEmailId, symbols);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping(value = "/profile/watchlist/remove")
    public ResponseEntity<?> removeSymbols(@RequestBody Map<String, List<String>> body,
                                        HttpServletRequest httpServletRequest) {
        String jwtEmailId = (String) httpServletRequest.getAttribute("emailId");
        List<String> symbols = body.get("symbols");
        if(symbols == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            userService.removeFromWatchlist(jwtEmailId, symbols);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
