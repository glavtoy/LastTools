package ru.lastlord.lasttools.crypto.util;

import org.bukkit.scheduler.BukkitRunnable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CryptoPriceChecker {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void updatePrice(Coin coin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                coin.setCost(getPrice(coin.getSymbol()).join());
            }
        }.runTaskAsynchronously(LastTools.instance);
    }

    private static CompletableFuture<Double> getPrice(String crypto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final HttpURLConnection connection = (HttpURLConnection) new URL("https://api.binance.com/api/v3/ticker/price?symbol=" + crypto.toUpperCase() + "USDT").openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    final StringBuilder builder = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) builder.append(inputLine);
                    in.close();
                    String jsonString = builder.toString();
                    int startIndex = jsonString.indexOf("price");
                    int endIndex = jsonString.indexOf("}", startIndex);
                    String priceString = jsonString.substring(startIndex + 7, endIndex);
                    priceString = priceString.replace("\"", ""); // Удаляем двойные кавычки
                    return Double.parseDouble(priceString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0.0d;
        }, executor);
    }
}
