package com.deco2800.game.components.tasks;

import com.deco2800.game.components.Component;

public class BountyComponent extends Component {
    private int coin;

    public BountyComponent(int coin) {
        this.coin = coin;
        setCoin(coin);
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        if (coin > 1) {
            this.coin = coin;
        }
    }
}
