package com.kims.goblinsis.model.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true)
    private String account;

    @Column(name = "password")
    private String password;

    private String name;

    private Date birth;

    private String phone;

    private String address;

    private String email;

    private String gender;  // F : 여자, M : 남자

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns        = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",  referencedColumnName = "id")}
    )
    private Role role;

    @Override
    @JsonProperty("account")
    public String getUsername() {
        return account;
    }
    public void setUsername(String account) {
        this.account = account;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Role userRoles = this.getRole();
        if(userRoles != null) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRoles.getRolename());
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof User) {
            final User other = (User) obj;
            return Objects.equal(getId(), other.getId())
                    && Objects.equal(getAccount(), other.getAccount())
                    && Objects.equal(getPassword(), other.getPassword())
                    && Objects.equal(getRole().getRolename(), other.getRole().getRolename())
                    && Objects.equal(isEnabled(), other.isEnabled());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getAccount(), getPassword(), getRole().getRolename(), isEnabled());
    }

}