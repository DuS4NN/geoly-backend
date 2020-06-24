package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "log")
public class Log {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private int createdAt;

    @Column(name = "data", columnDefinition = "VARCHAR(10000)")
    @NotNull
    private String data;

    @Column(name = "log_type", columnDefinition = "VARCHAR(20)")
    @NotNull
    private String logType;

    public Log() {
    }
}
