package com.yijiyap.modal;

import com.yijiyap.domain.VerificationType;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}