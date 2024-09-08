package com.yijiyap.controller;

import com.yijiyap.modal.User;
import com.yijiyap.modal.Wallet;
import com.yijiyap.response.WalletResponse;
import com.yijiyap.service.OrderService;
import com.yijiyap.service.UserService;
import com.yijiyap.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponse> deposit(
            @RequestHeader("Authorization") String jwt,
            @RequestBody BigDecimal depositAmount) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        Wallet updatedWallet = walletService.deposit(wallet, depositAmount);
        return ResponseEntity.ok(new WalletResponse(updatedWallet));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponse> withdraw(
            @RequestHeader("Authorization") String jwt,
            @RequestBody BigDecimal depositAmount) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        Wallet updatedWallet = walletService.withdraw(wallet, depositAmount);
        return ResponseEntity.ok(new WalletResponse(updatedWallet));
    }
}
