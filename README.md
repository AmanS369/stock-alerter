

# 📈 Stock Alert System (Event-Driven Architecture)

A modular, event-driven **Stock Price Alert System** built using **Spring Boot microservices**, **Apache Kafka**, and **real-time notifications** (Email / WhatsApp).

This project demonstrates:
- Microservices architecture
- Kafka event streaming
- Dead Letter Queues (DLQ)
- Shared internal library via `commonlib`
- Docker-based local environment
- Clean config separation using Spring Profiles

---

## 🚀 System Overview

The system lets users define price alerts for stocks.  
When live market data updates, alerts are matched and notifications are triggered.

Flow:
1. **Price Producer** fetches live stock data and publishes to Kafka.
2. **Alert Matcher** consumes price updates and checks alert conditions.
3. If alert condition is met → event is published.
4. **Notifier Service** listens and sends a notification (Email / WhatsApp).
5. Failed messages are routed to **Dead Letter Queue (DLQ)**.

---

## 🏛 Microservices

```

stock-alert-system/
├── alert-api         # REST API for user alerts
├── alert-matcher     # Matches stock prices to user alerts
├── commonlib         # Shared DTOs, utils, constants
├── notifier          # Sends notifications (Email / WhatsApp)
├── price-producer    # Generates price events and pushes to Kafka
├── scripts/          # Local scripts (ignored from Git)
├── docker-compose.yml
└── pom.xml           # Parent project

```

---

## 🔧 Tech Stack

- **Java 17**
- **Spring Boot**
- **Apache Kafka**
- **Spring Mail**
- **Twilio WhatsApp API**
- **Lombok**
- **Docker & Docker Compose**
- **Maven Multi-Module Build**

---

## 🧠 Key Concepts Learned

- Why & how to implement **Dead Letter Queues (DLQ)**
- Lua scripting and need in the current system with redis
- How to **set up a common library** (`commonlib`) for microservices
- Importance of structured logging
- Profile-based config (`application-local.yaml`)
- Kafka consumer groups & message strategies
- Strategy Pattern

---

## 🗄 Configuration Strategy

Committed configs:
```

application.yaml
application-dev.yaml
application-prod.yaml

```

Ignored (local-only):
```

application-local.yaml

```

Run profile:
```

SPRING_PROFILES_ACTIVE=local

````

---

## 🐳 Run with Docker (recommended)

1. Build modules:
```sh
mvn clean install
````

2. Start all services + Kafka:

```sh
docker-compose up -d
```

---

## 💌 Notifications Supported

* Email (Spring Mail)
* WhatsApp (Twilio API)

More can be added:

* SMS
* Telegram
* WebPush

---

## 🧱 Common Module (commonlib)

Contains:

* Shared DTOs
* Shared entities & repository
* Utils

---

## 🧪 Local Development

Every microservice supports:

```
application-local.yaml
```

This file is ignored from Git:

```
**/src/main/resources/application-local.yaml
```

Here’s the **section you can add to your README** — clean, technical, and explains exactly why Lua was used and how it fits your design.

---

## 🧩 Why Lua Scripts Are Used in This System

Redis supports Lua scripting for **atomic operations**, meaning multiple read–write steps can be executed as **one single, locked transaction**.
This is crucial when multiple alerts are processed in parallel and multiple services might try to update the same key at the same time.

In this system, Lua is used for:

### 1. Locking Per Strategy (to avoid race conditions)

Each alert strategy (like `STOCK_ABOVE` or `STOCK_BELOW`) has **its own Lua script**.
This ensures:

* Only one matcher can claim an alert at a time
* No duplicate notifications for the same condition
* No conflict when multiple threads or instances evaluate alerts

Example flow:

1. Alert condition is detected
2. Matcher service tries to claim it
3. Lua script checks & sets a lock atomically
4. Only the first successful lock wins — others skip

Without Lua, you would get:

* Double sends
* Collisions
* Partial updates
* Inconsistent data states

Lua gives you:

* Atomic check + set (in one step)
* No need for distributed locks or DB transactions
* Fast execution inside Redis itself

---

### 2. Single Lua Delete Script for All Strategies

After an alert is successfully published to Kafka, it must be **deleted atomically** so it isn't processed again.

Instead of many delete flows, there is **one common Lua script** that:

* Verifies the alert exists
* Removes it safely
* Works across all alert types

This ensures:

* No orphaned alerts
* No repeated notifications
* No race between “matcher-delete” and “notifier-send”

---

### Why Lua (Summarized)

* Atomicity: read + logic + write = one indivisible operation
* Performance: runs inside Redis, no client round-trip
* Simplicity: avoids distributed locking frameworks
* Correctness: guarantees exactly-once alert handling

Lua scripting makes Redis act like a tiny **transaction processor** tuned for concurrency and perfect for event-driven systems like this.


---

