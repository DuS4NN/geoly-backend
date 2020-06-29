package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

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
    private Date createdAt;

    @Column(name = "data", columnDefinition = "JSON")
    @NotNull
    private String data;

    @Column(name = "log_type", columnDefinition = "VARCHAR(20)")
    @NotNull
    private String logType;

    public Log() {
    }
}
