package com.yijiyap.service;

import com.yijiyap.modal.User;
import com.yijiyap.modal.Wallet;
import com.yijiyap.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet deposit(Wallet wallet, BigDecimal amount);

    Wallet withdraw(Wallet wallet, BigDecimal amount) throws Exception;

}
