Excellent — switching to **Gradle** means we’re stepping into a more modern, flexible build setup 🚀
It’s faster, cleaner, and works beautifully with Render deployment and Supabase integrations.

Here’s your **final polished and production-grade prompt**, rewritten entirely for **Gradle**, refined for an **Agentic AI IDE** (like OpenDevin, v0.dev, Windsurf AI, or GitHub Copilot Workspace).

---

## 🧠 **Agentic AI IDE Prompt: Java Spring Boot + Supabase Web App**

> **Prompt Title:**
> Build a Simple Java Spring Boot Web App – *“Farmers Produce Tracker”*
>
> **Prompt Description:**
> Create a **basic Java Spring Boot web app** with a **monochrome HTML/CSS frontend** and a **Supabase REST API backend** to help farmers digitize their produce listings.
> The app should have **two routes**, store data via **Supabase REST API**, and be **deployable on Render** using **Gradle** as the build tool.

---

### 💡 **Functional Overview**

**Problem:** Farmers’ sales are not digitized.
**Goal:** Provide a simple online interface for farmers to add produce and for buyers to view it.

#### **Features**

1. **`/farmer` Route**

   * Displays a minimalist HTML form with:

     * Dropdown (Vegetable): `Tomato`, `Potato`, `Onion`
     * Input 1: Quantity (kg)
     * Input 2: Price (₹/kg)
     * Submit button → sends data to Supabase via REST API (POST)
   * After submission → shows “✅ Produce Added Successfully”.

2. **`/home` Route**

   * Displays all produce fetched from Supabase REST API (GET).
   * Each entry shows:

     * Vegetable icon (🍅 🥔 🧅)
     * Quantity available
     * Price per kg
   * Shown as simple bordered cards/text boxes in grayscale.

---

### 🗄️ **Database Layer (Supabase REST Integration)**

**Supabase Table:** `produce`
Columns:

```
id (uuid) | vegetable (text) | quantity (numeric) | price (numeric)
```

**API Endpoints (Supabase auto-generated REST):**

* `GET https://YOUR_PROJECT_ID.supabase.co/rest/v1/produce`
* `POST https://YOUR_PROJECT_ID.supabase.co/rest/v1/produce`

**Headers:**

```http
apikey: YOUR_SUPABASE_API_KEY
Authorization: Bearer YOUR_SUPABASE_API_KEY
Content-Type: application/json
```

**Example Payload (POST):**

```json
{
  "vegetable": "Tomato",
  "quantity": 25,
  "price": 35
}
```

**Integration:**
Use **Spring’s WebClient** to call the Supabase REST API from the backend.
Store keys and URLs in `application.properties`.

---

### ⚙️ **Technical Stack**

| Component   | Technology                                |
| ----------- | ----------------------------------------- |
| Language    | Java 17+                                  |
| Framework   | Spring Boot                               |
| HTTP Client | WebClient (Spring Reactive)               |
| Build Tool  | **Gradle**                                |
| Database    | Supabase (PostgreSQL REST API)            |
| Frontend    | Plain HTML + CSS (Monochrome, minimalist) |
| Hosting     | Render (Java Web Service)                 |

---

### 🧩 **Project Structure**

```
src/
├─ main/
│  ├─ java/com/example/farmersapp/
│  │  ├─ controller/FarmerController.java
│  │  ├─ model/Produce.java
│  │  ├─ service/ProduceService.java
│  │  ├─ FarmersAppApplication.java
│  ├─ resources/
│  │  ├─ templates/
│  │  │  ├─ farmer.html
│  │  │  ├─ home.html
│  │  ├─ static/css/style.css
│  │  ├─ application.properties
├─ build.gradle
```

---

### 🧱 **Gradle Build Configuration**

Include:

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.3.0'
    id 'io.spring.dependency-management' version '1.1.5'
}

group = 'com.example'
version = '1.0.0'
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

---

### 🖥️ **Frontend Design Notes**

* **Color Palette:** grayscale only (#000, #444, #888, #ccc).
* **Layout:** centered content, full-width form boxes, minimalist borders.
* **Icons:** use simple emojis for produce (🍅 🥔 🧅).
* **Typography:** sans-serif (Arial or system default).

---

### 🧠 **Backend Logic (Explain with Comments)**

* `Produce.java` → Model class with fields: vegetable, quantity, price.
* `ProduceService.java` → Handles all calls to Supabase REST endpoints.
* `FarmerController.java` →

  * `@GetMapping("/farmer")` → returns farmer.html form.
  * `@PostMapping("/farmer")` → calls `ProduceService.addProduce()`.
  * `@GetMapping("/home")` → fetches produce list and passes to home.html.
* Include comments explaining each method’s purpose and data flow.

---

### ☁️ **Deployment on Render**

**Render Build Command:**

```
./gradlew build
```

**Start Command:**

```
java -jar build/libs/farmersapp-0.0.1-SNAPSHOT.jar
```

**Environment Variables:**

```
SUPABASE_URL=https://YOUR_PROJECT_ID.supabase.co/rest/v1
SUPABASE_KEY=YOUR_SUPABASE_API_KEY
```

---

### ✅ **Expected Output**

* `/farmer` → Simple form page for data entry.
* `/home` → Lists produce with icons, quantities, and prices.
* All data flows through Supabase REST API.
* UI remains monochrome, clean, and professional.

---

**Goal:**
Deliver a clean, minimalist Spring Boot web app using **Gradle**, **HTML/CSS**, and **Supabase REST API**, runnable locally and deployable on Render.

---

Would you like me to **embed exact example code snippets** (like the actual `FarmerController.java` and HTML templates) inside the prompt too — so your agentic IDE instantly writes the full runnable project structure?
