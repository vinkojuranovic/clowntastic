package com.ferit.clowntastic.models;

public class User {

    private Long id;

    private Long serverId;

    private String email;

    private Type type;

    private String firstName;

    private String lastName;

    public User(Long serverId, String email, Type type, String firstName, String lastName) {
        this.serverId = serverId;
        this.email = email;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(Long id, Long serverId, String email, Type type, String firstName, String lastName) {
        this.id = id;
        this.serverId = serverId;
        this.email = email;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName()
    {
        return this.firstName + " " + this.lastName;
    }
}
