package ru.lastlord.lasttools.manager;

import java.util.concurrent.ConcurrentHashMap;

public class StatisticManager {

    public static final ConcurrentHashMap<String, Integer> killsCache = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Integer> deathsCache = new ConcurrentHashMap<>();
    private final long CACHE_EXPIRATION_TIME = 60000;
    private final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
}
