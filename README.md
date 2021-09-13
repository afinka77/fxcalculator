## fxcalculator
## Currency exchange REST API

@author Alina Gončarko 

REST API implements "/v1/fx/calculate" endpoint designed to calculate
amount in resulting currency by given amount in source currency.

For calculation given CSV file is being used, having 6 currency exchange
rates provided in EUR currency.

#### Building project from command line window:
>.\gradlew clean build

#### Running project from command line window:
>.\gradlew bootRun
 
Example request:
```
http://localhost:8080/v1/fx/calculate?amount=10&fromCurrency=USD&toCurrency=EUR
```

Example result:
```
{
"fromAmount": 1,
"fromCurrency": "USD",
"toAmount": 0.809552722000000000,
"toCurrency": "EUR",
"rate": 0.809552722,
"dateTime": "2021-09-12T08:22:07.340737900Z"
}
```

#### Swagger documentation 

Swagger documentation is available via URL http://localhost:8080/swagger-ui.html
