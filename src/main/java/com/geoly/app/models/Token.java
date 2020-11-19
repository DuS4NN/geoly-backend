package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TOKEN")
public class Token {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "token", columnDefinition = "VARCHAR(100)")
    @NotNull
    private String token;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    @NotNull
    private TokenType action;

    public Token() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public TokenType getAction() {
        return action;
    }

    public void setAction(TokenType action) {
        this.action = action;
    }
}
