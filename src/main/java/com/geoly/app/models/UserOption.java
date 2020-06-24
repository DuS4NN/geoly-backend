package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "user_option")
public class UserOption {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Language.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    @NotNull
    private Language language;

    @Column(name = "dark_mode", columnDefinition = "TINYINT")
    @NotNull
    private boolean darkMode;

    @Column(name = "map_theme", columnDefinition = "TINYINT")
    @ColumnDefault("1")
    @NotNull
    private boolean mapTheme;

    @Column(name = "private_profile", columnDefinition = "TINYINT")
    @ColumnDefault("0")
    @NotNull
    private boolean privateProfile;

    public UserOption() {
    }
}
