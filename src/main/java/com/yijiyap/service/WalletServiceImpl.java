package com.yijiyap.service;

import com.yijiyap.modal.User;
import com.yijiyap.modal.Wallet;
import com.yijiyap.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (wallet == null) {
            Wallet newWallet = new Wallet();
            newWallet.setUser(user);
            walletRepository.save(newWallet);
            return newWallet;
        }
        return wallet;
    }

    @Override
    @Transactional
    public Wallet deposit (Wallet wallet, BigDecimal amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(amount);
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet withdraw(Wallet wallet, BigDecimal amount) throws Exception {
        BigDecimal balance = wallet.getBalance();

        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Insufficient balance to perform action.");
        }
        wallet.setBalance(balance);
        return walletRepository.save(wallet);
    }
}
