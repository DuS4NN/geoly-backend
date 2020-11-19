package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LOG")
public class Log {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "data", columnDefinition = "TEXT")
    @NotNull
    private String data;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    @NotNull
    private LogType logType;

    public Log() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }
}
