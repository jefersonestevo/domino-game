package br.com.game.dominoes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Domino implements Serializable, Comparable {

    private Integer upValue;
    private Integer downValue;

    private Domino(Integer upValue, Integer downValue) {
        this.upValue = upValue;
        this.downValue = downValue;
    }

    public static Domino[] getDominoes() {
        List<Domino> dominoes = new ArrayList<Domino>();

        for (int i = 0; i < 7; i++) {
            for (int j = 6; j >= 0; j--) {
                if (i >= j) {
                    dominoes.add(new Domino(i, j));
                }
            }
        }

        Collections.sort(dominoes);

        return dominoes.toArray(new Domino[dominoes.size()]);
    }

    public String getId() {
        return upValue + "_" + downValue;
    }

    public Integer getUpValue() {
        return upValue;
    }

    public Integer getDownValue() {
        return downValue;
    }

    public Integer getSummedValue() {
        return upValue + downValue;
    }

    public boolean isSameValue() {
        return upValue.equals(downValue);
    }

    @Override
    public int compareTo(Object o) {
        Domino other = (Domino)o;
        int compare = other.upValue.compareTo(this.upValue);
        if (compare == 0) {
            compare = other.downValue.compareTo(this.downValue);
        }
        return compare;
    }

    @Override
    public String toString() {
        return getId();
    }
}
