# The Movies Catalog Application

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Key Technical Decisions](#key-technical-decisions)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Introduction

The application created in this repo is a demo application which intention is to reflect
how a modern application should be structured.

## Features

The application allows the user:
* To see a list of latest movies as defined by the TMDBApi.
* To see the details of such movies when each of them are selected.
* Display the data in a way that can be read correctly depending on the orientation.
* Display the labels used in the application in English or Spanish.

## Key Technical Decisions

* Jetpack Compose
* Kotlin
* Code organized following latest Google's Architecture Guideline
* Unidirectional Data Flow
* Dependency Injection
* Storage Support using Room

All the explanation about each of the previous points will be addressed in the following [document](https://docs.google.com/document/d/1e8XEd2p1_ovLGcSCk9dVSUNOnhI3ZvHp36kxN9XKEbQ/edit?usp=sharing)

## Screenshots
Here are some screenshots of the app.

![Logo](/documentation/images/TMDB_Logo.png)
![Main Screen](/documentation/images/latest_movies.png)
![Details Screen](/documentation/images/movie_details.png)
![Main Screen - Landscape](/documentation/images/latest_movies_landscape.png)
![Details Screen - Landscape](/documentation/images/movie_details_landscape.png)

## Getting Started

### Prerequisites

List any prerequisites for running the project. This could include Android Studio, specific SDK
versions, etc.

- Java 1.8 or higher
- Android Studio 2024 or superior
- Android SDK version 29 & 35

### Installation

Provide step-by-step instructions on how to set up and run your project.

1. Clone the repository:
   ```sh
   git clone git@github.com:pperotti/MoviesCatalogApp.git
   ```
2. Open the project in Android Studio.
3. Build and run the application on an emulator or connected device.

## Usage

All you have to do is selecting the app's icon and wait for the app to load the list of most recent movies available.

Once the list is displayed, you can select an invididual movie to see its details.

All screens support a personalized dispalyed depending on the device's orientation.

## License

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/) - see the
[LICENSE.md](https://github.com/username/repository/blob/master/LICENSE.md) file for details.

## Acknowledgments

All the project created here was done by Pablo Perotti

- [github.com/pperotti] (GitHub)

```