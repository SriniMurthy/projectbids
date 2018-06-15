# MarketPlace

How to start the MarketPlace application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/projectbids-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

TO RUN THIS APP, PLEASE RUN THE FOLLOWING SCRIPT
 run startapp.sh

Following are the URIs for running the tests
a) Creating a project  (POST)

http://localhost:8080/projectbids/projects/
Here is a sample JSON doc for creating a new project

{"name":"GitHubPROJECT Feb2017",
 "description": "This is an expired Github project",
 "budget": 9815.75,
 "ownerID": "seenu@gmail.com",
 "ebdString":"02-22-2017",
 "projectID": -1 //OK, thats a 'feature':D 
 }
 
 b) Getting all projects in the database (GET)
 http://localhost:8080/projectbids/projects/
 
 c) Getting all "open" projects  (GET)
 http://localhost:8080/projectbids/projects/openprojects/
 
 d)Create a bid (POST)
 http://localhost:8080/projectbids/projectbids/createBid
 {"bidderName":"Srini Murthy","bidAmount":625.55,"projectID":4,"bidID":0}
 
 e) Getting all bids for a project({ID})   (GET)
 http://localhost:8080/projectbids/projectbids/3
 
 
 f) Deleting a project (DELETE)
 http://localhost:8080/projectbids/projects/delete/1
 
 g) Getting the cheapest Bid for a project  (GET)
 http://localhost:8080/projectbids/projectbids/getCheapestBid/1
 
4) Run your client
I used POSTMAN to test above cases, and all the fringe input cases, some of
these testcases I have provided screenshots. Apologies for the lack of time
to create a client.
# projectbids
