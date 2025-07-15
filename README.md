# 📊 Svampar

> 🚨 This is a production-grade inventory management system built for a client. Although not currently in use, the full application is shared here as part of my portfolio.

**Svampar** is a full-stack web application for managing inventory, users, and invoicing. It features a secure backend, responsive frontend, and email integration with PDF invoice generation.

---

## 🛠 Tech Stack

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-0095D5?style=flat&logo=kotlin&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=flat&logo=angular&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white)
![Heroku](https://img.shields.io/badge/Heroku-430098?style=flat&logo=heroku&logoColor=white)

---

## ✨ Features

- 🔒 JWT-based authentication and secure user sessions  
- 📦 Inventory management with full CRUD functionality  
- 📤 Email sending with auto-generated **PDF invoices**  
- 🖥️ Responsive UI built with Angular  
- 🧾 Invoice tracking and itemized exports  
- 🚀 Deployed to Heroku for easy delivery and testing  

---

## 🔧 Setup

### Backend (Ktor – `src/` folder)

```bash
cd src/
./gradlew build
java -jar build/libs/your-app.jar
cd ../m-client/
npm install
ng serve
