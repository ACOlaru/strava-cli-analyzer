# 🏃‍♂️ Strava CLI Analyzer

**Strava CLI Analyzer** is a command-line analytics tool built with **Java 17** and **Spring Boot 3**.  
It connects to the **Strava REST API** to fetch, analyze, and forecast athletic activity data — delivering actionable insights directly in the terminal.

---

## 📘 Overview

This project provides a structured, maintainable, and data-driven backend designed around the Strava ecosystem.  
It demonstrates how to combine **modern Java**, **Spring Boot modularization**, and **statistical computation** into a clean and extensible CLI-based platform.

**Design Highlights**
- Clear separation of service responsibilities
- Reusable data models and clean API layer
- Robust token and configuration management
- Ready extension path toward REST or Web UI

---

## 🧩 System Architecture

The system follows a **layered, modular design** to ensure scalability and maintainability.

Key Principles

- Each component has a single, clear purpose.

- Stateless operation — no database dependency.

- Lightweight persistence through JSON token storage.

- Structured foundation for future REST or UI extensions.

## ⚙️ Key Capabilities

Data Access 
  - Integration with Strava’s REST API 
  - Secure, local token storage and refresh 
  - Robust error handling for network and auth failures

Analytics
  - Aggregated distance, duration, elevation, and speed statistics 
  - Best week and streak detection 
  - Weekly summaries and average performance trends 
  - Predictive Insights 
  - Linear regression–based forecasts for future performance 

Extendable architecture for advanced ML models or historical trend analysis

## 🧱 Technologies

- Java 17 
- Spring Boot 3 
- Maven
- HttpClient
- Jackson
- Apache Commons Math 3 Regression 
- java-dotenv

## 🚀 Example Workflow
* User provides a valid Strava access token
* Application fetches recent activities
* Statistics are computed automatically:
  * Total & average distance
  * Longest activity
  * Average speed and elevation gain
  * Weekly summaries & streaks
* Predictions are generated via regression models
* Results are displayed in the terminal

## ⚙️ Setup & Usage
Prerequisites
Java 17+
Maven
Valid Strava API credentials (client ID, secret, redirect URI)

Installation


* git clone https://github.com/ACOlaru/strava-cli-analyzer.git
* cd strava-cli-analyzer
* mvn clean install
* Running the Application 
  * mvn spring-boot:run 
* Environment Configuration 
  * Define your Strava credentials in a .env file:
`STRAVA_CLIENT_ID=your_client_id
STRAVA_CLIENT_SECRET=your_client_secret
STRAVA_REDIRECT_URI=your_redirect_url`


## 🔮 Future Enhancements
* OAuth2-based web authorization flow
* Historical activity caching and CSV export
* Enhanced data visualization dashboards
* Integration with RESTful or cloud-based backends
* Advanced predictive modeling (e.g., time-series forecasting)

## Learn More

I wrote a detailed article about this project on Medium, explaining the architecture, design patterns, and concurrency challenges:

[From Strides to Stats: Building My Own Strava CLI Analytics Tool in Java](https://medium.com/@olarualexandra/from-strides-to-stats-building-my-own-strava-cli-analytics-tool-in-java-3f792d8e7baa)


## ⚖️ License
Licensed under the MIT License.
