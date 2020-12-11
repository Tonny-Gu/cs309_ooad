package com.sustech.dboj.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user_info")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;// True Name
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    @JsonIgnore
    private String password;// password after encoding
    @Column(nullable = false)
    private String role;// Three types:STU/TA/SA
    @ManyToMany(mappedBy = "users")
    @JsonIgnore //关掉不重要的一边的序列化，防止循环引用
    private Set<Contest> contests;

    @JsonIgnore
    private String activeCode;

    public User() {
    }

    public Set<Contest> getContests() {
        return contests;
    }

    public void setContests( Set<Contest> contests ) {
        this.contests = contests;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }


    public void setUsername( String username ) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname( String nickname ) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role ) {
        this.role = role;
    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode( String activeCode ) {
        this.activeCode = activeCode;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }//是否过期

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }//是否冻结

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] authorities = role.split( "," );
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>( );
        for (String role : authorities) {
            simpleGrantedAuthorities.add( new SimpleGrantedAuthority( role ) );
        }
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", contests=" + contests +
                ", activeCode='" + activeCode + '\'' +
                '}';
    }
}
