package com.uni.bankproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table (
    name = "bank_user",
    schema = "dbo"
)
@Data
public class User implements UserDetails {

    @Id
    private String userID;

    @Column(name="username")
    @NotNull
    private String userName;

    @Column(name="email")
    @NotNull
    private String email;

    @Column(name="phone_number")
    @NotNull
    private String phoneNumber;

    @Column(name="address")
    @NotNull
    private String address;

    // Hashed password
    @Column(name="user_key")
    @NotNull
    private String userKey;

    @Column(name="bank_admin")
    @NotNull
    private boolean admin = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userKey;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
