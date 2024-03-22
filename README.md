# Little Pay Code Challenge

This project is designed to process a csv file that has a list of taps and generates an output file of trips.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)

## Installation

This project is written with Java 17 and uses maven to build. 

The ```cost file path``` and ```tap file path``` are stored at ```src/main/resources``` folder. It would be better to define an environment variable 
to set the path, but as the reviewers will clone the project on their local, so modifying the values in this way would
be easier.


## Usage
Please run the application from ```CodeChallengeApplication``` file.

## Configuration
You can modify ```Tap.csv``` file to add/remove different taps and then run the app again, the generated content will
be written to the existing ```trip.csv``` file. 

