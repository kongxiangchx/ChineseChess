package com.example.a77304.chessgame;

import java.io.Serializable;

/**
 * Created by 77304 on 2021/4/4.
 */

public class Pos implements Cloneable, Serializable {
    private static final long serialVersionUID = 3572115210886077953L;

    int x;
    int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pos)) {
            return false;
        }
        Pos pos = (Pos) obj;
        return this.x == pos.x && this.y == pos.y;
    }
}
