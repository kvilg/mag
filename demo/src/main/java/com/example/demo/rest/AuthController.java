package com.example.demo.rest;



import com.example.demo.model.ExceptionModel;
import com.example.demo.model.User;
import com.example.demo.model.UserAuth;
import com.example.demo.model.UserOut;
import com.example.demo.security.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.demo.servis.UserService;
import static com.example.demo.model.Constants.TOKEN_PREFIX;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private UserService service;



    @PostMapping(value="/login")
    public ResponseEntity<?> getAuthUser(@RequestBody UserAuth json) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(json.getLogin(), json.getPassword()));
            com.example.demo.model.User user = service.getByLogin(json.getLogin()).orElseThrow(() -> new Exception("User not found"));;
            String token = jwtTokenUtil.generateToken(user);
            UserOut g = new UserOut(user, TOKEN_PREFIX + token);
            return new ResponseEntity<>(g, HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.UNAUTHORIZED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @GetMapping(path = "/login/jwt")
    public ResponseEntity<?>  getUserByJWT(@RequestHeader("Authorization") String token) {

        try {
            String a = token.substring(TOKEN_PREFIX.length());
            String userLogin = jwtTokenUtil.getUsernameFromToken(a);
            User user = service.getByLogin(userLogin).orElseThrow(() -> new Exception("User not found"));;
            return new ResponseEntity<>(new UserOut(user) ,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.UNAUTHORIZED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/registration")
    public ResponseEntity<?>  registration(@RequestBody UserAuth json) throws Exception {
        try {
            service.registration(json.getEmail(),json.getName(),json.getLogin(),json.getPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/active")
    public ResponseEntity<?> active(@RequestBody UserAuth json) {
        try {
            service.activeUser(json.getLogin(),json.getCode());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExceptionModel(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


/** для того что бы токен авторизировал нужен префикс Bearer {token}
 *
 * PS. ЭТО ТОЛЬКО ДЛЯ ЗНАЧЕНИЯ AUTORIZAIFION NE JWT
 * */
}
