# hotel-java-api

A hotel API for managing check in. The API main URL `/hotel/`.

## How API works?

The API creates, updates, deletes and lists guests and check ins. It also calculates the subsistence of the guests. The API has following endpoints:

`POST/hotel/guests` – endpoint to create a guest.

`PUT/hotel/guests/{id}` – endpoint to update a guest.

`GET/hotel/guests` – returns the guests created.

`GET/hotel/guests/{id}` – returns the guest by the id.

`GET/hotel/guests/bySaida` – returns the guests that still in or not.

`DELETE/hotel/guests` – deletes all guests.

### Details

`POST/hotel/checkins` – endpoint to create\save a check in.

`GET/hotel/checkins/{id}` – returns the check in by the id.

`GET/hotel/checkins/byNome` – endpoint to find a guest by nome.

`GET/hotel/checkins/byDocumento` – endpoint to find a guest by documento.

`GET/hotel/checkins/byTelefone` – endpoint to find a guest by telefone.

`GET/hotel/checkins/byTelefone` – endpoint to find a guest by telefone.

**Body:**

<code>
{
  "id": 1,
  "nome": "Joao",
  "documento": "333.333.333.33",
  "telefone": "99999999"
}
</code>

**Where:**

`id` - guest id; 


Returns an empty body with one of the following:

201 – in case of success
400 – if the JSON is invalid
422 – if any of the fields are not parsable or the transaction date is in the future

`PUT/hotel/guests/{id}`

This endpoint is called to update a guest.

**Body:**

<code>
{
   "id": 1,
   "nome": "Joao",
   "documento": "333.333.333.33",
   "telefone": "99999999"
}
</code>

Must be submitted the object that will be modified. Must return a guest specified by ID and all fields recorded above, including links and
the one that was updated.

<code>
{   
   "id": 1,
   "nome": "Joao",
   "documento": "333.333.333.33",
   "telefone": "99999999"
   "links": [
	    {
	        "rel": "self",
	        "href": "http://localhost:8080/hotel/guests/1"
	    }
   ]
}
</code>

`POST/hotel/checkins`

This endpoint creates a check in.

**Body:**

<code>
{
  "hospede": 
  {
    "id": 1,
    "nome": "Victor",
    "documento": "01103",
    "telefone": "9999999"
  },
  "dataEntrada": "2020-04-05T05:48:23.000Z",
  "dataSaida": "2020-04-06T20:48:23.000Z",
  "adicionalVeiculo": true
}
</code>
 
**Where:**

`hospede` – the guest object.

`dataEntrada` – Entrance day;.

`dataSaida` – Exit day; the system will use both days to calculate the stay of the guest.

`adicionalVeiculo` – If the guest uses the hotel parking lot, he will pay too.

### Test

* For unit test phase, you can run:

```
mvn test
```

* To run all tests (including Integration Tests):

```
mvn integration-test
```

### Run

In order to run the API, run the jar simply as following:

```
Change the application.properties to adapt to your database configuration
```

```
java -jar hotel-java-api-1.0.0.jar --spring.profiles.active=prod
```
    
or

```
mvn spring-boot:run -Dspring.profiles.active=prod
```

By default, the API will be available at [http://localhost:8080/](http://localhost:8080/)
