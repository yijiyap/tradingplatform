package com.yijiyap.service;

import com.yijiyap.modal.Coin;

import java.util.List;

public interface CoinService {
    List<Coin> getCoinList(int page);

    String getMarketChart(String coinId, int days);

    String getCoinDetails(String coinId);

    String searchCoin(String keyword);

    String getTop50CoinsByMarketCapRank();

    String getTrendingCoins();

    Coin getCoinById(String coinId);
}
