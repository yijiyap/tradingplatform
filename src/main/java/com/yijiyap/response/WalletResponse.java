package com.yijiyap.response;

import com.yijiyap.modal.Wallet;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponse {
    private BigDecimal balance;

    public WalletResponse(Wallet wallet) {
        this.balance = wallet.getBalance();
    }
}
