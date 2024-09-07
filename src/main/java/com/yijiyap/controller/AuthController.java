package com.yijiyap.controller;

import com.yijiyap.config.JwtProvider;
import com.yijiyap.modal.User;
import com.yijiyap.repository.UserRepository;
import com.yijiyap.response.AuthResponse;
import com.yijiyap.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

//        Check if User already exists
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // Create new User object based on data provided in response.
        User newUser = new User(user.getFullName(), user.getEmail(), user.getPassword());
        User savedUser = userRepository.save(newUser);

//        Create new authentication auth. No need authorities because Spring will load it later.
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());


//        Log in the user once they sign up.
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Registration successful");

        // Returns saved user along with HTTP status code.
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        try {

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

            if (userDetails == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            if (!userDetails.getPassword().equals(user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());

            //        Log in the user if authenticated.
            SecurityContextHolder.getContext().setAuthentication(auth);

            //        Generate JWT
            String jwt = JwtProvider.generateToken(auth);

            //        Build response
            AuthResponse response = new AuthResponse();
            response.setJwt(jwt);
            response.setStatus(true);
            response.setMessage("Login successful");

            // Returns saved user along with HTTP status code.
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }
}

