package br.com.game.dominoes.model;

import java.io.Serializable;

public class Player implements Serializable {
    private String id;
    private String nickName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
