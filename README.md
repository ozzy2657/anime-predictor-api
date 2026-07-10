# Anime Rating Predictor — Spring Boot API

Part 2 of 2 of the Anime Rating Predictor project. This repo contains a Spring Boot REST API that loads a cleaned anime dataset and predicts anime ratings using a linear regression model trained in R.

**Part 1 (R Data Pipeline):** [anime_predictor](https://github.com/ozzy2657/anime_predictor)

---

## What This Does

Exposes a REST API endpoint that accepts anime attributes (episodes, members, genre, type) and returns a predicted MAL rating based on a multivariate linear regression model trained in R.

**Example request:**
```
GET /animes/predict?episodes=37&members=3400000&genre=Mystery&type=TV
```

**Example response:**
```json
{ "predictedRating": 8.3 }
```

---

## How the Prediction Works

The prediction formula comes directly from the R model's `summary(lm())` output:

```
predicted_rating = 5.948                          ← intercept
                 + type_coefficient               ← e.g. TV = +0.222
                 + sum(genre_coefficients)        ← e.g. Mystery = +0.386
                 + (episodes × 0.0004963)
                 + (members  × 0.0000005)
```

This is called **porting the model** — R handles training, Java handles inference. The coefficients are hardcoded as constants in `AnimeService.java`, meaning no ML library is needed at runtime.

---

## Architecture

```
cleaned_anime.csv
       │
       ▼
AnimeService.java        ← loads CSV, stores records, runs prediction formula
       │
       ▼
AnimeController.java     ← exposes REST endpoints, validates input, returns JSON
       │
       ▼
GET /animes/predict      ← browser/Postman/any HTTP client
POST /animes/predict     ← for programmatic API calls
```

---

## Project Structure

```
src/main/java/com/AnimeProject/demo/
  ├── DemoApplication.java      ← Spring Boot entry point
  ├── AnimeRecorder.java        ← POJO representing one CSV row
  ├── AnimeService.java         ← CSV loading + regression prediction logic
  └── AnimeController.java      ← REST endpoints with input validation
```

---

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 26 | Primary language |
| Spring Boot 4.1 | REST API framework |
| Maven | Build tool and dependency management |
| OpenCSV | CSV parsing library |
| R (separate repo) | Model training and data cleaning |

---

## API Reference

### `GET /animes/predict`
Returns a predicted rating for a given anime profile.

**Query parameters:**

| Parameter | Type | Example | Description |
|---|---|---|---|
| `episodes` | int | `37` | Number of episodes |
| `members` | int | `3400000` | MAL member count |
| `genre` | String | `Mystery` | Primary genre |
| `type` | String | `TV` | Anime type (TV, Movie, OVA, ONA, Special, Music) |

**Response:**
```json
{ "predictedRating": 8.3 }
```

**Error response (invalid input):**
```json
{ "error": "episodes must be greater than 0" }
```

---

## Sample Predictions

| Anime | Episodes | Members | Genre | Type | Predicted | Actual MAL |
|---|---|---|---|---|---|---|
| Death Note | 37 | 3,400,000 | Mystery | TV | ~8.3 | 8.62 |
| Sword Art Online | 25 | 1,800,000 | Action | TV | ~7.6 | 7.20 |
| One Punch Man | 12 | 1,500,000 | Action | TV | ~7.5 | 8.52 |

---

## How to Run

**Requirements:** Java 21+, Maven, `cleaned_anime.csv` from the R pipeline repo

```bash
# Clone the repo
git clone https://github.com/ozzy2657/anime-predictor-api.git
cd anime-predictor-api/demo

# Place cleaned_anime.csv in the demo/ folder

# Run
mvn spring-boot:run
```

API will be available at `http://localhost:8080/animes/predict`

---

## What I Learned

- Polyglot architecture — using R and Java together, each for what it does best
- Spring Boot dependency injection (`@Service`, `@RestController`)
- REST API design with input validation and structured JSON responses
- Maven dependency management via `pom.xml`
- CSV parsing and data type handling in Java
- Porting a statistical model from R to Java without ML libraries
