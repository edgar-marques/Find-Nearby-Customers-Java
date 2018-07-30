# Find nearby customers

Command line utility for finding all customers in a user-supplied file (in JSON format) which are within a given radius (in km) of a given location (in degrees lat/long). 

This project was started as an exercise in writing a production-ready CLI in Java 8 and explore the Java ecosystem including, but not limited to: dependency management, IoC and dependency injection, bean and method validation, bean mapping, JSON serialization/deserialization, logging, unit testing and CLI testing. 

This project is still a work in progress.

**TODO**

* Improve error handling
* Increase test coverage
* Write Javadoc comments and generate javadoc JAR
* Update README file


## Usage

**TODO** 

```
java -jar cli-1.0-SNAPSHOT-jar-with-dependencies.jar

Option (* = required)               Description
---------------------               -----------
-?, -h, --help                      show help
* --input-file <File>               customer file
* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target
                                      location
* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target
                                      location
-r, --radius <BigDecimal>           radius of proximity (in km) (default: 100.0)
-v, --verbose                       verbose mode  
```

## Getting Started

**TODO** These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

**TODO** What things you need to install the software and how to install them

Oracle JDK 8+ and Maven 3.5.4+

```
Give examples
```

### Installing

**TODO** A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

**TODO** End with an example of getting some data out of the system or using it for a little demo

## Running the tests

**TODO** Explain how to run the automated tests for this system

### Break down into end to end tests

**TODO** Explain what these tests test and why

```
Give an example
```

### And coding style tests

**TODO** Explain what these tests test and why

```
Give an example
```

## Deployment

**TODO** Add additional notes about how to deploy this on a live system

## Built With

* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - SDK
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **[Edgar Marques](https://github.com/edgar-marques)**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

**TODO** 
* Hat tip to anyone whose code was used
* Inspiration
* etc
