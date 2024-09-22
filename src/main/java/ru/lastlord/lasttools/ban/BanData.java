package ru.lastlord.lasttools.ban;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BanData {

    private String admin;
    private long time;
    private String reason;
}
