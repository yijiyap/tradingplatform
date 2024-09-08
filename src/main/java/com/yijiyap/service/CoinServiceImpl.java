package com.yijiyap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijiyap.modal.Coin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Coin> getCoinList(int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+page;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return objectMapper.readValue(response.getBody(),
                    new TypeReference<List<Coin>>(){});
        } catch (HttpClientErrorException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCoinDetails(String coinId) {

        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String searchCoin(String keyword) {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Failed to search coin. Please check the input keyword.", e);
        }
    }

    @Override
    public String getTop50CoinsByMarketCapRank() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalStateException("Failed to fetch top 50 coins by market cap.", e);
        }
    }

    @Override
    public String getTrendingCoins() {
        String url = "https://api.coingecko.com/api/v3/search/trending?vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalStateException("Failed to fetch trending coins.", e);
        }
    }

    @Override
    public Coin getCoinById(String coinId) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            Coin coin = new Coin();
            coin.setId(jsonNode.get("id").asText());
            coin.setName(jsonNode.get("name").asText());
            coin.setSymbol(jsonNode.get("symbol").asText());
            coin.setImage(jsonNode.get("image").asText());

            JsonNode marketData = jsonNode.get("market_data");

            coin.setCurrentPrice(BigDecimal.valueOf(marketData.get("current_price").get("usd").asDouble()));
            coin.setMarketCap(BigDecimal.valueOf(marketData.get("market_cap").get("usd").asDouble()));
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setFullyDilutedValuation(BigDecimal.valueOf(marketData.get("fully_diluted_valuation").get("usd").asDouble()));
            coin.setTotalVolume(BigDecimal.valueOf(marketData.get("total_volume").get("usd").asDouble()));

            coin.setHigh24h(BigDecimal.valueOf(marketData.get("high_24h").get("usd").asDouble()));
            coin.setLow24h(BigDecimal.valueOf(marketData.get("low_24h").get("usd").asDouble()));
            coin.setPriceChange24h(BigDecimal.valueOf(marketData.get("price_change_24h").asDouble()));
            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
            coin.setMarketCapChange24h(BigDecimal.valueOf(marketData.get("market_cap_change_24h").asDouble()));
            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());

            coin.setCirculatingSupply(BigDecimal.valueOf(marketData.get("circulating_supply").asDouble()));
            coin.setTotalSupply(BigDecimal.valueOf(marketData.get("total_supply").asDouble()));
            coin.setMaxSupply(BigDecimal.valueOf(marketData.get("max_supply").asDouble()));
            coin.setAth(BigDecimal.valueOf(marketData.get("ath").get("usd").asDouble()));

            return coin;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
