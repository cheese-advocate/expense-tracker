# Expense Tracker

**My Grails expense tracker coding challenge project for tracking expenses against users.**

---

## Table of Contents

- [Features](#features)  
- [Requirements](#requirements)  
- [Installation](#installation)  
- [Configuration](#configuration)  
- [Running the Application](#running-the-application)  
- [Usage](#usage)
- [Optimisations at Scale](#optimisations-at-scale)
- [License](#license)

---

## Features

- Built with [Grails 5.3.2](w) and [Groovy](w)  
- Responsive UI with [GSP](w) views  
- CSV export of transaction data  
- Exchange rate support using optional [Fixer.io](w) API  
- Tracks expenses per user with a running balance  
- Self-contained full stack web application (no REST APIs)

---

## Requirements

- [OpenJDK 11.0.27](w)  
- [Grails 5.3.2](w)  
- [Gradle 7.2](w)  
- Uses embedded [H2 Database](w) (no external DB required)  
- Optional: Fixer.io API key

---

## Installation

```bash
# Clone the repository
git clone https://github.com/cheese-advocate/expense-tracker.git
cd expense-tracker

# Compile the project
./gradlew compileGrails
```

---

## Configuration

The Fixer.io API key for USD conversion is optional.

There are two ways to provide it (if not as a Gradle bootRun arg):

### 1. Environment Variable

```bash
export FIXER_API_KEY=yourapikeyhere
```

### 2. `secrets.properties` file

Create a file named `secrets.properties` in the project root:

```
fixer.api.key=yourapikeyhere
```

This file is automatically loaded by the application if it exists.

---

## Running the Application

Run with Grails CLI:

```bash
grails run-app
```

Or with Gradle (including optional API key):

```bash
./gradlew bootRun -Dfixer.api.key=yourapikeyhere
```

---

## Usage

- Navigate to `http://localhost:8080`  
- Create a new user  
- Add expense transactions to the user  
- View running ZAR and USD balances  
- Export transaction data as CSV  
- No login or authentication is required  

---

## Optimisations at Scale

The current application is currently built for local or small-scale use. With that said, possible optimisations to support 10k users using the system concurrently among others are listed below:

- GORM optimisations (eg. indexing, pagination, minimising query complexity).
- Using a more scalable RDBMS solution such as MySQL or PostGreSQL for database interactions.
- Pooling database and API connections to reduce overhead from transactions and API calls.
- Decoupling CRUD transactions from non-time-critical transient API calls to reduce backend load and allow for asynchronous computing, or even queueing for non-time-critical operations.
- Adding caching for optimising reads which can be eventually consistent from the database.
- Containerising the server and orchestrating the containers (eg. Kubernetes) for scaling available server instances.
- Deploying behind a load balancer with sticky sessions to spread traffic between backend instances.
- Implementing profiling and monitoring tools (eg. Prometheus) to track performance and system health under load.

As an additional note, optimising the frontend for improved user experience would also be a consideration, such as for improved information filtering and handling in tables, for example.

---

## License

This project is licensed under the [MIT License](w).

### Compliance Checklist:

To remain compliant with the MIT license:

- Include a `LICENSE` file with the full MIT text (see below).
- Retain the license notice in all source files if you copy significant code elsewhere.
- Give credit to this repository if others reuse or distribute it.

Here is the recommended `LICENSE` file content:

```
MIT License

Copyright (c) 2025 Tristan Ackermann

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
