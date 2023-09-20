package com.ekart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "user", schema = "public")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue
    private int userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String emailId;
    private String contactNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String password;
    @Transient
    private String confirmPassword;
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private RoleType role;
//    @OneToOne(targetEntity = Account.class,fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "accountFK")
//    private Account account;

    @OneToMany(targetEntity = Address.class,mappedBy = "user",fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany
    private List<Orders> orders = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.emailId;
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
