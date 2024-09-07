package com.yijiyap.modal;

import com.yijiyap.domain.UserRole;
import com.yijiyap.modal.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructor() {
        User user = new User("Test User 1", "test@user1.com", "abc123");
        assertNotNull(user);

        assertEquals("Test User 1", user.getFullName());
        assertEquals("test@user1.com", user.getEmail());
        assertEquals("abc123", user.getPassword());
        assertEquals(UserRole.ROLE_CUSTOMER, user.getRole());
        assertNotNull(user.getTwoFactorAuth());
    }

    @Test
    @DisplayName("Not create user when fullname is blank")
    void testUserValidationWithBlankName(){
        assertThrows(IllegalArgumentException.class, () -> new User("", "test@user1.com", "abc123"));
    }

}