# tradingplatform
A project to learn the Spring Boot framework and test-driven development.

## Functional Requirements:
1. Retail traders can place market buy orders
2. Retail traders can place market sell orders
3. Retail traders can close their orders
4. Balance to be reflected in their account.

## Coin Service:
### Get all coins
GET: `/coins`
Query Parameters:
- page (int, optional): Page number for pagination
- size (int, optional): Number of items per page

### Get Coin Details
GET: `/coins/{coinId}`

### Get Trending Coins
GET: `/coins/trending`

## Wallet Service
### View Balance
GET: `/api/wallet`

### Update Wallet Balance
POST: `/api/wallet/deposit`
POST: `/api/wallet/withdraw`
Request body:
```
{
"amount": "number",
}
```

## Orders Service
### Get pending orders
GET: `/api/orders/pending`

### Place order
POST: `POST: /api/orders/place`

### Close order
POST: `/api/orders/{orderId}/close`

