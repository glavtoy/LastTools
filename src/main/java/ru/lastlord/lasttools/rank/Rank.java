package ru.lastlord.lasttools.rank;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Rank {
    private String tag;
    private boolean isDefault;
    private int index;
    private String prefix;
    private List<String> permissions;
}
