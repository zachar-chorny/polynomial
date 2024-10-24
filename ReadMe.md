# Polynomial Processing App

## Build Instructions

To build the application using Maven, run the following command in the root directory of the project:

```sh
mvn clean install
```

## Running with Docker

To run the application in docker container, run the following command in the root directory of the project:

```sh
docker-compose up --build
```

## API Endpoints

### Simplify Polynomial

**Endpoint:** `/polynomial/simplify`  
**Method:** `POST`

**Request Body:**

```text
3*x^2+4*x-5
```

### Evaluate Polynomial

**Endpoint:** `/polynomial/value`  
**Method:** `POST`

**Request Body:**

```json
{
  "polynomial": "2x + 3",
  "value": 5
}

