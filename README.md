# (Never) Ride on Time

Application for scrapping and analyzing data about delays of Cracov's buses.

## Run
```dockerfile
    sudo docker-compose up
```
Runs database instance and also scraper daemon as containers. You can run app modules through:
```sbtshell
    sbt "project analyzer" run
    sbt "project scraper" run
``` 
