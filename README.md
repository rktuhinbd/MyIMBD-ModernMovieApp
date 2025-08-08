# MyIMBD – Modern Movie App

An Android Developer Interview Test Project showcasing a modern, well-architected movie app built with Kotlin and Jetpack Compose.

---

## Overview

MyIMBD fetches movie data from a remote source once during the splash screen and caches it locally with Room. After the initial fetch, it runs fully offline. Users can browse movies, search, filter by genre, add to wishlist, and paginate the list in batches of 10.

### Screens:
1. Splash Screen  
2. Movie List Screen  
3. Movie Details Screen  
4. Wishlist Screen  

Movie data source:  
[https://raw.githubusercontent.com/erik-sytnyk/movies-list/master/db.json](https://raw.githubusercontent.com/erik-sytnyk/movies-list/master/db.json)

---

## Features

| Feature           | Description                                                         |
|-------------------|---------------------------------------------------------------------|
| List Movie        | Scrollable list of movies in reverse chronological order.           |
| Search Movie      | Search movies by title or content.                                  |
| Filter Movie      | Filter movies via dropdown by genres.                               |
| Splash Page       | Load & cache remote data once.        |
| Wishlist          | Add/remove movies to/from wishlist with animation.                  |
| Pagination        | Load 10 movies per batch from local DB.                             |
| Local Storage     | Persistent storage using Room database.                             |
| MVVM / Clean Arch | ViewModel and Repository pattern (Clean Architecture).             |
| State Management  | StateFlow & Compose State to drive UI reactively.                   |
| Dependency Injection | Hilt for DI.                                                    |

---

## Tech Stack

- Kotlin  
- Jetpack Compose (Material3)  
- Room Database  
- Retrofit  
- Hilt  
- StateFlow / Compose State  
- Minimum SDK 24, Target SDK 36  

---

## Architecture

- **UI Layer:** Compose UI observing ViewModel state flows  
- **ViewModel:** Handles UI events and manages state  
- **UseCases:** Encapsulate business logic  
- **Repository:** Abstracts data sources (API & Room)  
- **Data Layer:** Room DAO and Retrofit API service  
- **DI:** Managed by Hilt  

---

## Setup Instructions

1. Clone the repo:  
   ```bash
   git clone https://github.com/your-username/myimbd-modern-movie-app.git
   cd myimbd-modern-movie-app
