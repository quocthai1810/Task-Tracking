package com.example.task_tracker.domain.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth")
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_auth", nullable = false, updatable = false)
    private UUID idAuth;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String passWord;

    @OneToMany(mappedBy = "auth", cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    private List<TaskList> taskLists;

    @Column(name = "role", nullable = false)
    private List<String> role;

    public UUID getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(UUID idAuth) {
        this.idAuth = idAuth;
    }

    public String getUserName() {
        return userName;
    }

    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Auth() {
    }

    @Override
    public String toString() {
        return "Auth [idAuth=" + idAuth + ", userName=" + userName + ", passWord=" + passWord + ", role=" + role + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idAuth == null) ? 0 : idAuth.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((passWord == null) ? 0 : passWord.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Auth other = (Auth) obj;
        if (idAuth == null) {
            if (other.idAuth != null)
                return false;
        } else if (!idAuth.equals(other.idAuth))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (passWord == null) {
            if (other.passWord != null)
                return false;
        } else if (!passWord.equals(other.passWord))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

    public Auth(UUID idAuth, String userName, String passWord, List<String> role) {
        this.idAuth = idAuth;
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

}
