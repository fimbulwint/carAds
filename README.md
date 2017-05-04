
Hi! Thanks for reviewing my code. A few remarks on the solution.

### Notes

 * The endpoints follow the usual REST conventions, please see the `routes` file for the particular URLs.
 * I chose to use `int` for the ids. In the past I have normally assigned UUIDs on the server side during the creation of a new resource. But since the field was marked as
  *required* in the requirements, I decided to leave it to the client to generate them.
 * The JSON of a car advert is of the form:
 ```json
     {
       "id": 1,
       "title": "Audi A4",
       "fuel": "diesel",
       "price": 40000,
       "new": false,
       "mileage": 10000,
       "firstReg": "2018-09-12"
     }
    ```
    where *mileage* and *firstReg* are only required if the advert is for a new car. All fields are validated.
 * The ads can be updated using the POST endpoint. I didn't have time to add a PATCH operation for updates.
 * When retrieving the list of all ads, it is possible to send a query parameter `sort=<sortField>` to return the ads in ascending order by the specified field. All common
 fields can be used, but not *mileage* and *firstReg* since new car ads do not have them. If the field is unknown or the param is not passed in the call, then the sorting
 defaults to use *id*
 * I added the CORS filter, but that triggered a strange reflection runtime exception that I didn't have the time to fix, so I ended up removing it.
 * The backend service uses a DynamoDB table called *CarAds* with a numeric primary key called *id*. The application will create the table dynamically if it doesn't exist.

### Tests

The app contains a suite of basic acceptance tests. I didn't have time to add unit tests unfortunately.

While I worked on the task, I was both running `sbt test` from command line and executing the suite from my IDE (Eclipse using `sbteclipse` plugin) But at some point
`sbt test` started failing from some issue related to Guice that I couldn't solve. If you run into the same problem (Guice exceptions from command line tests), please
run the tests from the IDE instead.

### Running the code

The service runs on the default port `9000`

The application uses the [DefaultAWSCredentialsProviderChain](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html) to connect to AWS, so you will need
to provide the credentials using one of the available mechanisms described in the docs.

To run the application, you can run `sbt run` from the base directory.