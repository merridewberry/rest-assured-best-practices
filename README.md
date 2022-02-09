### Use specifications

Constructions such as

    given()
    .accept(ContentType.JSON)

are used in almost every request. Even if it's only two lines, you still can cut them in half by calling a single method - and make your code more readable by doing so. For that you can either use a separate method:

    public static RequestSpecification requestSpec() {
        return given()
            .accept(ContentType.JSON);
    }

or builder:

    RequestSpecification requestSpec = new RequestSpecBuilder()
        .setAccept(ContentType.JSON)
        .build();

And just call this method or use this builder in every request.

Though it's highly unlikely that these two lines would be the only code you could move to specifications. You can add there some authorization headers, set host and port, set up logging, add Allure filters, and do whatever else you wish to do in every request.

This will make your code much easier to understand, because the person who reads it most likely doesn't really want to be reminded that you authenticated and set content type every single time. For example, this is the request specification I used in my recent project:

    public static RequestSpecification requestSpec() {
        return given()
                .filter(new AllureRestAssured())
                .header("User-Agent", USER_AGENT)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth().oauth2(getProperty("oauth-token"))
    }

You do the same thing with response too. Your options are a bit more limited in this case, but you still can check response code and time, and whether receiver response contains some specific headers and fields. Just like with request specification, you could use either method: 

    public static ValidatableResponse responseSpecOk(Response response) {
        return response
            .then()
            .statusCode(200)
            .time(lessThanOrEqualTo(responseTime));
    }

or builder:

    ResponseSpecification responseSpecOk = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectResponseTime(lessThanOrEqualTo(responseTime))
            .build();

### Store your endpoints in a separate place

You shouldn't add endpoints directly into the method where you want to use them. Even when there are not many of them or if this specific endpoint is only used in your code once, if you'll ever need to change it, something is quite likely to get lost, and in the end you'll spend more time on finding the cause of your problem than on setting you endpoints nice and tidy to begin with.

You can either create a separate class to manage your endpoints or just store them in some properties file. Something like this:

    public class Endpoints {
        public static String booking = "booking";
        public static String bookingId = "booking/{id}";
        public static String auth = "auth";
    }

If your endpoint has some changeable part (an ID for example), you can store it this way:

    String endpoint = "endpoint/{id}"

In this case you'll just need to pass the value you want to use as an argument:

    .get(endpoint, id)

### Store your base URI in a separate place

Just like headers, some part of the URL is present in every one of your requests - so you can as well store it separately and initiate it just once.

You can either put it inside some class as a variable or use a properties file - I prefer the latter.

There are two ways of initializing base URI:

    given()
        .baseUri(getProperty("baseUri"))

and:

    RestAssured.baseURI = getProperty("baseUri");

And speaking of calling the base URI, you also have two options.

First - to call it in every request. This way you'll have more code, but if you have to use few different base URIs it'll have to do.

Second - to call it once, for example, inside the request specification.

### Use REST Assured built-in functional for converting objects

There is no need to convert POJO into Json using some external libraries because REST Assured is perfectly capable of doing it by itself. You'll just need to add you POJO into the request body:

    Pojo pojo = new Pojo();
    
    given()
        .body(pojo)
        .post(URI);

It can convert Json into POJO as well:

    Pojo pojo = given()
        .get(URI)
        .then()
        .extract().body().as(Pojo.class)

And there's more! REST Assured can convert Hash Maps into Json as well - even nested ones:

    Map <Object, Object> outerMap = new HashMap<Object, Object>();
    Map <Object, Object> innerMap = new HashMap<Object, Object>();

    innerMap.put("key", "value");

    outerMap.put("key", "value");
    outerMap.put("innerMap", innerMap);

    given()
        .body(outerMap)
        .post(URI);

### Use Json schema validation

Json schema validation is a simple way to make sure that the data you've received has all the expected fields and variables are of correct types.

For example, for Json like this:

    {
        "id": 1,
        "item": {
            "key1": "integer or sting",
            "key2": "string",
            "key3": "not required"
        }
    }

Json schema will look like this:

    {
        "type": "object",
        "properties": {
            "id": {
                "type": "integer"
            },
            "item": {
                "type": "object",
                "properties": {
                    "key1": {
                        "type": ["string", "integer"]
                    },
                    "key2": {
                        "type": "string"
                    },
                    "key3": {
                        "type": "string"
                    }
                },
                "required": ["key1", "key2"]
            }
        },
        "required": ["id", "item"]
    }

This schema makes sure that received Json has all the fields that were marked as required and all the values in the fields has the required type.

You can write Json schemas by yourself, though they are a bit tricky and not very human-friendly, so I'd strongly recommend you to use one of the many online tools for creating schemas and not waste your time. For example, I use this one: https://www.liquid-technologies.com/online-json-to-schema-converter

To use Json schema validation you'll need a json-schema-validator library (I use the one from io.rest-assured) and the following code:

    .body(matchesJsonSchemaInClasspath("schema.json"));

You can find more information about Json schema here: https://json-schema.org/

### Move repeatedly performed actions to a separate method

There's no need to check whether the response is valid ten times for ten tests. You may as well just make one separate method and call it every time you need it.

This applies to any type of frequently performed actions. It's much easier to read and maintain the code where all the constructors, specifications and checks exist in separate blocks with which you can build your tests. 

Like this for example:

    public static RequestSpecification requestSpec = new RequestSpecBuilder()
        .setBaseUri(BASE_URI)
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();

    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectResponseTime(lessThanOrEqualTo(responseTime))
                .build();
    }
    
    public static Response createItem(Pojo body) {
        return given()
            .spec(requestSpec)
            .body(body)
            .post(Uri);
        }

    public static ValidatableResponse createItem_valid(Response response) {
        return response
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("schemas/CreateItem.json"));
    }

    @Test
    public void createItemTest() {
        createItem_valid(
            createItem(body))
    }

### Use Hamcrest Matchers

When there's no real need to deserialize response into POJO, matchers from the Hamcrest library are the fastest and easiest way to validate some specific values in the response.

They allow you to check whether the response contains some value at all, or if there is some value in specific field, or whether the array contains the items you need.

    .body("id", equals(getId()))
    .body("id", greaterThanOrEqualTo(1))
    .body("items", either(hasItem("a")).and(hasItem("b"))

Of course there are other ways around it, but I find using Hamcrest matchers the most convenient one.

You can find full Hamcrest Matcher documentation here: http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html

### Use preexisting REST Assured methods to add headers

There is a shorter and neater way to add headers for content type, accepted type, authorization and much more: 

	given()
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)

Apart from being shorter, these methods allow you to use preexisting enums, and this way no typos may occur in your code.

Though note that, for example, using enum *ContentType.JSON* in the *accept()* method results in sending not only *application/json* content type, but also *application/javascript*, *text/javascript* and *text/json*. This may cause some issues in certain APIs, so look out for that, maybe you'll need to set content type using sting.

### Check response status without assertThat

There's no need to type  

    .assertThat(statusCode(200))

the shorter option does the job just fine:

    .statusCode(200)

### Pass status codes as arguments

If you have quite a few negative tests with different expected status codes, you may end up with the bunch of similar methods to check them. Instead of that you can just pass expected status code as an argument and use just one method for all tests:

    public static ResponseSpecification responseSpecError(int statusCode) {
        return new ResponseSpecBuilder()
            .expectStatusCode(statusCode)
            .expectResponseTime(lessThanOrEqualTo(responseTime))
            .build();
    }

### Use logging for debugging tests

*log()* method allows you to get information about both sent request and received response. To receive request log, you'll need to place it before the method that you use to send a request (*get()*, *post()*, etc.), to receive response log - after it.

There are the following methods available:

    .log().all()
    .log().params()
    .log().body()
    .log().headers()
    .log().cookies()

Though you shouldn't leave these logging methods in your tests after you've got them up and running. The amount of data you receive may be so big that all your test results won't fit inside IDE console, and even if they'll do, it will be quite hard to read.