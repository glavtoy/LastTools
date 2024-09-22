package ru.lastlord.lasttools.crypto.economy.coin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Balance {

    private Coin coin;
    private double amount;
}
