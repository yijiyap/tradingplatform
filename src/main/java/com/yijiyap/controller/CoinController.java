package com.yijiyap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijiyap.modal.Coin;
import com.yijiyap.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {
        if (page <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Coin> coinList = coinService.getCoinList(page);
        return new ResponseEntity<>(coinList, HttpStatus.OK);
    }

    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode>getMarketChart(@PathVariable("coinId") String coinId, @RequestParam("days") int days) throws Exception {
        String response = coinService.getMarketChart(coinId, days);
        JsonNode node = objectMapper.readTree(response);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode>getCoinDetail(@PathVariable("coinId") String coinId) throws Exception {
        String coin = coinService.getCoinDetails(coinId);
        JsonNode node = objectMapper.readTree(coin);

        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoins(@RequestParam("q") String keyword) throws JsonProcessingException {
        String response = coinService.searchCoin(keyword);
        JsonNode node = objectMapper.readTree(response);

        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() throws Exception {
        String coin = coinService.getTop50CoinsByMarketCapRank();
        JsonNode node = objectMapper.readTree(coin);

        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @GetMapping("/trending")
    ResponseEntity<JsonNode> getTrendingCoins() throws Exception {
        String response = coinService.getTrendingCoins();
        JsonNode node = objectMapper.readTree(response);

        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}
