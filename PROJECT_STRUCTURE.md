# 📁 Complete Project Structure

This document shows the complete folder structure with all important files.

---

## 🗂️ Root Directory

```
api-monitoring-platform/
│
├── 📄 README.md                      # Main documentation
├── 📄 SETUP.md                       # Step-by-step setup guide
├── 📄 ENV_SETUP_CHECKLIST.md        # .env configuration checklist
├── 📄 PROJECT_STRUCTURE.md           # This file
├── 📄 .gitignore                     # Git ignore rules
│
├── 📁 collector-service/             # Backend: Central Collector
├── 📁 demo-monitoring-service/       # Backend: Demo Microservice
└── 📁 dashboard-ui/                  # Frontend: Next.js Dashboard
```

---

## 📦 Collector Service Structure

```
collector-service/
│
├── 📄 .env                           # ⚠️ YOUR MONGODB CREDENTIALS (create this!)
├── 📄 .env.example                   # Template for .env
├── 📄 pom.xml                        # Maven dependencies
├── 📄 mvnw                           # Maven wrapper (Mac/Linux)
├── 📄 mvnw.cmd                       # Maven wrapper (Windows)
│
├── 📁 .mvn/                          # Maven wrapper files
│   └── wrapper/
│
└── 📁 src/
    ├── 📁 main/
    │   ├── 📁 kotlin/
    │   │   └── com/monitoring/collectorservice/
    │   │       │
    │   │       ├── 📄 CollectorServiceApplication.kt
    │   │       │
    │   │       ├── 📁 config/
    │   │       │   ├── MongoConfig.kt           # ⭐ Dual MongoDB setup
    │   │       │   ├── SecurityConfig.kt        # JWT & CORS
    │   │       │   └── DataInitializer.kt       # Creates default user
    │   │       │
    │   │       ├── 📁 model/
    │   │       │   ├── 📁 logs/
    │   │       │   │   ├── ApiLog.kt
    │   │       │   │   └── RateLimitEvent.kt
    │   │       │   └── 📁 metadata/
    │   │       │       ├── User.kt
    │   │       │       └── Alert.kt             # ⭐ Optimistic locking
    │   │       │
    │   │       ├── 📁 repository/
    │   │       │   ├── 📁 logs/
    │   │       │   │   ├── ApiLogRepository.kt
    │   │       │   │   └── RateLimitEventRepository.kt
    │   │       │   └── 📁 metadata/
    │   │       │       ├── UserRepository.kt
    │   │       │       └── AlertRepository.kt
    │   │       │
    │   │       ├── 📁 dto/
    │   │       │   ├── AuthDtos.kt
    │   │       │   ├── ApiLogDto.kt
    │   │       │   └── AlertDto.kt
    │   │       │
    │   │       ├── 📁 service/
    │   │       │   ├── CustomUserDetailsService.kt
    │   │       │   ├── AuthService.kt
    │   │       │   ├── LogCollectorService.kt   # ⭐ Collects logs
    │   │       │   └── AlertService.kt          # ⭐ Alert generation
    │   │       │
    │   │       ├── 📁 controller/
    │   │       │   ├── AuthController.kt        # Login endpoint
    │   │       │   ├── CollectorController.kt   # Receives logs
    │   │       │   ├── LogController.kt         # Dashboard APIs
    │   │       │   └── AlertController.kt       # Alert APIs
    │   │       │
    │   │       └── 📁 security/
    │   │           ├── JwtTokenProvider.kt      # JWT generation
    │   │           └── JwtAuthenticationFilter.kt
    │   │
    │   └── 📁 resources/
    │       └── 📄 application.yml               # Reads from .env
    │
    └── 📁 test/
        └── kotlin/
```

**⚠️ Key Files:**
- `.env` - **YOU MUST CREATE THIS** with your MongoDB credentials
- `MongoConfig.kt` - Sets up dual MongoDB connections
- `AlertService.kt` - Implements optimistic locking for concurrency

---

## 📦 Demo Monitoring Service Structure

```
demo-monitoring-service/
│
├── 📄 .env                           # Service configuration (create this!)
├── 📄 .env.example                   # Template for .env
├── 📄 pom.xml
├── 📄 mvnw / mvnw.cmd
│
└── 📁 src/
    ├── 📁 main/
    │   ├── 📁 kotlin/
    │   │   └── com/monitoring/demo/
    │   │       │
    │   │       ├── 📄 DemoMonitoringServiceApplication.kt
    │   │       │
    │   │       ├── 📁 config/
    │   │       │   └── MonitoringConfig.kt      # Registers interceptor
    │   │       │
    │   │       ├── 📁 tracking/
    │   │       │   ├── ApiTrackingInterceptor.kt  # ⭐ Captures requests
    │   │       │   ├── RateLimiter.kt             # ⭐ Rate limiting
    │   │       │   └── MonitoringClient.kt        # Sends to Collector
    │   │       │
    │   │       ├── 📁 dto/
    │   │       │   └── ApiLogDto.kt
    │   │       │
    │   │       └── 📁 controller/
    │   │           └── DemoApiController.kt       # 3 demo APIs
    │   │
    │   └── 📁 resources/
    │       └── 📄 application.yml                 # Reads from .env
    │
    └── 📁 test/
```

**⚠️ Key Files:**
- `.env` - **CREATE THIS** (usually just copy from .env.example)
- `ApiTrackingInterceptor.kt` - Intercepts all API requests
- `RateLimiter.kt` - Implements token bucket rate limiting

---

## 📦 Dashboard UI Structure

```
dashboard-ui/
│
├── 📄 .env.local                     # Dashboard API URL (create this!)
├── 📄 .env.example                   # Template for .env.local
├── 📄 package.json                   # Node dependencies
├── 📄 tsconfig.json                  # TypeScript config
├── 📄 tailwind.config.ts             # Tailwind CSS config
├── 📄 next.config.js                 # Next.js config
│
├── 📁 app/
│   ├── 📄 layout.tsx                 # Root layout
│   ├── 📄 page.tsx                   # Home (redirect)
│   ├── 📄 globals.css                # Global styles
│   │
│   ├── 📁 login/
│   │   └── 📄 page.tsx               # Login page
│   │
│   └── 📁 dashboard/
│       └── 📄 page.tsx               # ⭐ Main dashboard
│
├── 📁 components/
│   ├── 📄 StatsCard.tsx              # Stats widget
│   ├── 📄 LogsTable.tsx              # Logs table
│   └── 📄 AlertsPanel.tsx            # Alerts with resolve
│
├── 📁 lib/
│   └── 📁 api/
│       └── 📄 client.ts              # ⭐ API client with JWT
│
├── 📁 types/
│   └── 📄 index.ts                   # TypeScript types
│
├── 📁 public/                        # Static assets
└── 📁 node_modules/                  # Dependencies (auto-generated)
```

**⚠️ Key Files:**
- `.env.local` - **CREATE THIS** with Collector API URL
- `client.ts` - Axios client with JWT interceptor
- `page.tsx` (dashboard) - Main dashboard with all features

---

## 🔐 Environment Files (⚠️ IMPORTANT!)

### Files You MUST Create:

1. **`collector-service/.env`**
   ```env
   LOGS_MONGO_URI=mongodb+srv://...
   METADATA_MONGO_URI=mongodb+srv://...
   ```

2. **`demo-monitoring-service/.env`**
   ```env
   SERVER_PORT=8081
   COLLECTOR_URL=http://localhost:8080/api/collector
   ```

3. **`dashboard-ui/.env.local`**
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080
   ```

### Template Files (Already in repo):

- `collector-service/.env.example`
- `demo-monitoring-service/.env.example`
- `dashboard-ui/.env.example`

**📖 Complete setup:** [ENV_SETUP_CHECKLIST.md](ENV_SETUP_CHECKLIST.md)

---

## 🗄️ MongoDB Atlas Structure

Your MongoDB Atlas should have:

```
📊 MongoDB Cluster: api-monitoring-cluster
│
├── 📁 monitoring_logs (Database 1)
│   ├── 📄 api_logs (Collection)
│   └── 📄 rate_limit_events (Collection)
│
└── 📁 monitoring_metadata (Database 2)
    ├── 📄 users (Collection)
    └── 📄 alerts (Collection)
```

---

## 📝 Files NOT in Git (.gitignore)

These files are excluded from GitHub for security:

```
❌ collector-service/.env
❌ demo-monitoring-service/.env
❌ dashboard-ui/.env.local
❌ node_modules/
❌ target/
❌ .next/
❌ *.log
```

**✅ Safe to commit:**
- `.env.example` files (templates without real credentials)
- All source code files
- Configuration files (pom.xml, package.json, etc.)

---

## 🎯 Key Components

### Backend (Spring Boot + Kotlin)
- **25 files** in Collector Service
- **9 files** in Demo Service
- **Maven wrapper** for easy building

### Frontend (Next.js + TypeScript)
- **8 pages/layouts**
- **3 main components**
- **Tailwind CSS** for styling

### Total Project Size
- **~40 Kotlin files**
- **~15 TypeScript files**
- **~2,500 lines of code**
- **3 services** running simultaneously

---

## 🚀 Quick Reference

**Start services:**
```bash
# Terminal 1
cd collector-service && mvnw.cmd spring-boot:run

# Terminal 2
cd demo-monitoring-service && mvnw.cmd spring-boot:run

# Terminal 3
cd dashboard-ui && npm run dev
```

**Access points:**
- Collector API: http://localhost:8080
- Demo APIs: http://localhost:8081
- Dashboard: http://localhost:3000

**Default credentials:**
- Username: `admin`
- Password: `admin123`

---

**📖 For setup instructions:** [SETUP.md](SETUP.md)

**📖 For .env help:** [ENV_SETUP_CHECKLIST.md](ENV_SETUP_CHECKLIST.md)