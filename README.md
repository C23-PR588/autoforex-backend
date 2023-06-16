# AutoForex Backend CC

Endpoint: http://nomadic-grid-382206.et.r.appspot.com 

## Auth

### Register URL /registrations
* Method POST | Request Body JSON

```json
{
    "firstName": "Test",
    "lastName": "Domain",
    "email": "test@gmail.com",
    "password": "letmein"
}
```
Respone Success

### Login URL /login
* Method GET
- [x] Email
- [x] Password

Response "login success" (if login is successful) "login fail" (else)

### Login URL /logout
* Method GET

Response wil redirect to login page (if logot is successful) "logout fail" (else)

## Wallet

### Check Wallet User URL /wallet/getCurrencies/{username}
* Method GET

Response wil return wallet user (if check wallet is successful) 404 (else)

```json
{
    "IDR": 282610.51,
    "USD": 1.0
}
```

### Buy Currency URL /wallet/buyCurrency
* Method POST | Request Params

```json
{
    "username": {username},
    "currencyName": "USD",
    "currencyValue": 10,
    "target": 17000
}
```
Response wil return "Wallet updated successfully." (if check buy currency is successful) 404 (else)

### Buy Currency URL /wallet/sellCurrency
* Method POST | Request Params

```json
{
    "username": {username},
    "currencyName": "USD",
    "currencyValue": 10
}
```
Response wil return "Currency sold successfully." (if check sell currency is successful) 404 (else)

## Currency

### Currencies Data URL /currencies/data
* Method GET

Response wil return currency trade value (if check currencies data is successful) 404 (else)

```json
{
    "AUD": 10253.959046,
    "SGD": 11150.356406,
    "JPY": 106.251117,
    "EUR": 16313.307464,
    "GBP": 19055.053073,
    "USD": 14904.029446,
    "CAD": 11270.979886,
    "MYR": 3221.791623,
    "RUB": 178.533927,
    "CNY": 2092.964753
}
```
