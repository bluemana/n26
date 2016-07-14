# RESTful API code challenge

Implementation of a custom RESTful web service based on the following API description.

## API

The API adheres to the REST constraints, is based on HTTP and uses a custom JSON media type.

### Examples

#### Get the base resource

**Request**

```
GET /
```

**Response**

```
HTTP 200 OK
```

#### Get the transaction service

**Request**

```
GET /transactionservice
```

**Response**

```
HTTP 200 OK
```

#### Create a transaction

**Request**

```
PUT /transactionservice/transaction/3

{
  "parent_id": 1,
  "type": "car",
  "amount": 15000
}
```

**Response**

```
HTTP 200 OK

{
  "status": "ok"
}
```


#### Get a transaction

**Request**

```
GET /transactionservice/transaction/3
```

**Response**

```
HTTP 200 OK

{
  "parent_id": 1,
  "type": "car",
  "amount": 15000
}
```

#### List all transactions

**Request**

```
GET /transactionservice/transactions
```

**Response**

```
HTTP 200 OK

{
  "transactions": [
    {
      "id": 1,
      "type": "shopping",
      "amount": 10.5
    },
    {
      "id": 2,
      "parent_id": 1,
      "type": "groceries",
      "amount": 12
    },
    {
      "id": 3,
      "parent_id": 1,
      "type": "car",
      "amount": 15000
    }
  ]
}
```

#### List all transaction IDs by type

**Request**

```
GET /transactionservice/types/shopping
```

**Response**

```
HTTP 200 OK

[1]
```

#### Get the transitive sum of a transaction

The API defines the transitive sum of a transaction ```t``` as the sum of ```t```'s amount and the amounts of all transactions that can be transitively linked to ```t``` by their ```parent_id``` field.

**Request**

```
GET /transactionservice/sum/1
```

**Response**

```
HTTP 200 OK

{
  "sum": 15022.5
}
```

**Request**

```
GET /transactionservice/sum/2
```

**Response**

```
HTTP 200 OK

{
  "sum": 12
}
```
