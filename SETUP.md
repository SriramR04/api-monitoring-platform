# 🚀 Quick Setup Guide

**⚠️ IMPORTANT: Follow these steps IN ORDER. Don't skip any step!**

---

## 📋 Prerequisites

Before starting, make sure you have:

- ✅ **Java 22+** installed - [Download Here](https://adoptium.net/temurin/releases/?version=22)
  - Verify: `java -version` (should show version 22 or higher)
- ✅ **Node.js 18+** installed - [Download Here](https://nodejs.org/)
  - Verify: `node --version` (should show v18 or higher)
- ✅ **Git** installed - [Download Here](https://git-scm.com/)

---

## 🎯 Step 1: Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/api-monitoring-platform.git
cd api-monitoring-platform
```

---

## 🗄️ Step 2: Setup MongoDB Atlas (CRITICAL!)

### 2.1 Create Free MongoDB Atlas Account

1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas/register)
2. Sign up with Google/GitHub or email
3. Choose **FREE (M0)** tier
4. Select **AWS** and choose a region near you
5. Cluster Name: `api-monitoring-cluster` (or any name)
6. Click **Create Deployment**
7. **SAVE** the username and password shown (you'll need them!)

### 2.2 Create Database User

1. In Atlas, go to **Database Access** (left sidebar)
2. Click **Add New Database User**
3. Authentication Method: **Password**
4. Username: `project_user` (or your choice)
5. Password: `ProjectPass123` (or your choice - **SAVE THIS!**)
6. Database User Privileges: **Atlas admin** (or "Read and write to any database")
7. Click **Add User**

### 2.3 Allow Network Access

1. Go to **Network Access** (left sidebar)
2. Click **Add IP Address**
3. Click **Allow Access from Anywhere** (0.0.0.0/0)
   - ⚠️ For production, use specific IPs. This is for development.
4. Click **Confirm**
5. Wait 1-2 minutes for changes to apply

### 2.4 Create Two Databases

1. Go to **Database** (left sidebar)
2. Click **Browse Collections**
3. Click **Add My Own Data**
4. Database Name: `monitoring_logs`
5. Collection Name: `api_logs`
6. Click **Create**
7. Repeat: Create another database named `monitoring_metadata` with collection `alerts`

### 2.5 Get Connection String

1. Click **Connect** on your cluster
2. Choose **Connect your application**
3. Driver: **Java** (or any)
4. Copy the connection string - looks like:
   ```
   mongodb+srv://project_user:<password>@cluster.mongodb.net/?retryWrites=true&w=majority
   ```
5. **Replace `<password>` with your actual password** (e.g., `ProjectPass123`)
6. **SAVE THIS STRING** - you'll need it in the next step!

**Your final connection strings should look like:**
```
mongodb+srv://project_user:ProjectPass123@api-monitoring-cluster.88wlsdc.mongodb.net/monitoring_logs?retryWrites=true&w=majority
mongodb+srv://project_user:ProjectPass123@api-monitoring-cluster.88wlsdc.mongodb.net/monitoring_metadata?retryWrites=true&w=majority
```

---

## ⚙️ Step 3: Configure Environment Variables

### 3.1 Collector Service Configuration

```bash
cd collector-service
```

**On Windows:**
```bash
copy .env.example .env
```

**On Mac/Linux:**
```bash
cp .env.example .env
```

Now **EDIT** `collector-service/.env` with your actual MongoDB credentials:

```env
# Replace with YOUR MongoDB Atlas connection strings from Step 2.5
LOGS_MONGO_URI=mongodb+srv://YOUR_USERNAME:YOUR_PASSWORD@YOUR_CLUSTER.mongodb.net/monitoring_logs?retryWrites=true&w=majority
METADATA_MONGO_URI=mongodb+srv://YOUR_USERNAME:YOUR_PASSWORD@YOUR_CLUSTER.mongodb.net/monitoring_metadata?retryWrites=true&w=majority

# Keep these as-is (or change JWT_SECRET for security)
JWT_SECRET=myVerySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256Algorithm
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

**⚠️ CRITICAL**: 
- Replace `YOUR_USERNAME` with your MongoDB username (e.g., `project_user`)
- Replace `YOUR_PASSWORD` with your MongoDB password (e.g., `ProjectPass123`)
- Replace `YOUR_CLUSTER` with your cluster address (e.g., `api-monitoring-cluster.88wlsdc.mongodb.net`)
- Make sure `/monitoring_logs` and `/monitoring_metadata` are AFTER the cluster address

**Example of correct .env:**
```env
LOGS_MONGO_URI=mongodb+srv://project_user:ProjectPass123@api-monitoring-cluster.88wlsdc.mongodb.net/monitoring_logs?retryWrites=true&w=majority
METADATA_MONGO_URI=mongodb+srv://project_user:ProjectPass123@api-monitoring-cluster.88wlsdc.mongodb.net/monitoring_metadata?retryWrites=true&w=majority
JWT_SECRET=myVerySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256Algorithm
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

### 3.2 Demo Service Configuration

```bash
cd ../demo-monitoring-service
```

**On Windows:**
```bash
copy .env.example .env
```

**On Mac/Linux:**
```bash
cp .env.example .env
```

**Edit** `demo-monitoring-service/.env` (usually no changes needed):

```env
SERVER_PORT=8081
SPRING_APPLICATION_NAME=demo-service
COLLECTOR_URL=http://localhost:8080/api/collector
RATE_LIMIT_SERVICE=demo-service
RATE_LIMIT_VALUE=100
RATE_LIMIT_ENABLED=true
```

### 3.3 Dashboard Configuration

```bash
cd ../dashboard-ui
```

**On Windows:**
```bash
copy .env.example .env.local
```

**On Mac/Linux:**
```bash
cp .env.example .env.local
```

**Edit** `dashboard-ui/.env.local` (usually no changes needed):

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## 📦 Step 4: Install Dependencies

### 4.1 Backend Services (Maven)

**Collector Service:**
```bash
cd collector-service
mvnw.cmd clean install -DskipTests
# Mac/Linux: ./mvnw clean install -DskipTests
```

Wait for "BUILD SUCCESS"

**Demo Service:**
```bash
cd ../demo-monitoring-service
mvnw.cmd clean install -DskipTests
# Mac/Linux: ./mvnw clean install -DskipTests
```

Wait for "BUILD SUCCESS"

### 4.2 Frontend Dashboard (Node.js)

```bash
cd ../dashboard-ui
npm install
```

Wait for installation to complete (2-3 minutes)

---

## 🚀 Step 5: Run the Application

**⚠️ IMPORTANT: Start services in THIS ORDER!**

You need **3 separate terminal windows/tabs**:

### Terminal 1: Start Collector Service (MUST START FIRST!)

```bash
cd collector-service
mvnw.cmd spring-boot:run
# Mac/Linux: ./mvnw spring-boot:run
```

**Wait for these messages:**
```
==================================================
🚀 Initializing Collector Service...
✅ Default user created: username=admin, password=admin123
==================================================
Started CollectorServiceApplication in 8.XXX seconds
```

**⚠️ If you see MongoDB connection errors:**
- Go back to Step 2 and verify:
  - Network Access allows your IP (0.0.0.0/0)
  - Database user has correct permissions
  - Connection strings in `.env` are correct (username, password, cluster address)
  - Databases `monitoring_logs` and `monitoring_metadata` exist in Atlas

**Don't proceed until you see "Started CollectorServiceApplication"!**

### Terminal 2: Start Demo Service (Start AFTER Collector is running)

```bash
cd demo-monitoring-service
mvnw.cmd spring-boot:run
# Mac/Linux: ./mvnw spring-boot:run
```

**Wait for:**
```
============================================================
🚀 Demo Monitoring Service Started on port 8081
============================================================
📌 Available Endpoints:
   GET http://localhost:8081/api/demo/success  (Fast API)
   GET http://localhost:8081/api/demo/slow     (Slow API - Triggers Alert)
   GET http://localhost:8081/api/demo/error    (Error API - Triggers Alert)
   GET http://localhost:8081/api/demo/health   (Health Check)
============================================================
```

### Terminal 3: Start Dashboard (Start LAST)

```bash
cd dashboard-ui
npm run dev
```

**Wait for:**
```
✓ Ready in 2.5s
○ Local:   http://localhost:3000
```

---

## 🎯 Step 6: Access the Dashboard

1. Open browser: **http://localhost:3000**
2. You'll be redirected to login page
3. Login with default credentials:
   - **Username**: `admin`
   - **Password**: `admin123`
4. Click **Sign In**

**If login succeeds**, you'll see the dashboard! 🎉

---

## 🧪 Step 7: Generate Test Data

To see the system in action, open these URLs in **new browser tabs**:

**1. Success API (Fast, no alert):**
```
http://localhost:8081/api/demo/success
```
Call this 3-4 times

**2. Slow API (Triggers SLOW API alert):**
```
http://localhost:8081/api/demo/slow
```
Call this 2 times (wait 850ms each time)

**3. Error API (Triggers BROKEN API alert):**
```
http://localhost:8081/api/demo/error
```
Call this 2 times

**4. Refresh Dashboard:**
- Go back to http://localhost:3000/dashboard
- Press F5 to refresh (or wait 10 seconds for auto-refresh)
- You should see:
  - Stats cards updated (7 total logs, 2 slow, 2 broken)
  - Active alerts showing slow and broken APIs
  - Logs table with all API calls

---

## 🔥 Step 8: Test Rate Limiter (Optional)

To trigger rate limit violations:

1. Open browser console: Press **F12** → **Console** tab
2. Paste this code and press Enter:

```javascript
for(let i = 0; i < 120; i++) {
  fetch('http://localhost:8081/api/demo/success');
}
```

This sends 120 requests in 1 second, exceeding the 100 req/sec limit!

3. Refresh dashboard to see rate limit alerts

---

## ✅ Success Checklist

If everything is working, you should see:

- [ ] All 3 terminal windows showing "Started" or "Ready"
- [ ] Dashboard at http://localhost:3000 shows login page
- [ ] Can login with admin/admin123
- [ ] Dashboard shows 4 stat cards
- [ ] Calling demo APIs creates logs
- [ ] Slow/Error APIs create alerts
- [ ] Can click "Resolve" on alerts
- [ ] MongoDB Atlas shows data in collections

---

## 🐛 Troubleshooting

### Problem: Collector Service won't start

**Error:** `MongoTimeoutException` or `MongoSocketException`

**Solution:**
1. Check MongoDB Atlas **Network Access** allows 0.0.0.0/0
2. Verify username/password in `.env` are correct
3. Test internet connection
4. Wait 2 minutes after adding IP to Network Access

**Error:** `Could not resolve placeholder 'LOGS_MONGO_URI'`

**Solution:**
1. Make sure `.env` file exists in `collector-service/` folder
2. Restart the service
3. Check `.env` file has no typos

### Problem: Dashboard shows "Loading..." forever

**Solution:**
1. Check Collector Service is running (Terminal 1)
2. Open http://localhost:8080/api/auth/health - should return `{"status":"UP"}`
3. Check browser console (F12) for errors
4. Logout and login again

### Problem: "Cannot find module '@/components/StatsCard'"

**Solution:**
1. This is just VS Code warning - app still works!
2. Press Ctrl+Shift+P → "TypeScript: Restart TS Server"
3. Or ignore it - doesn't affect functionality

### Problem: Port already in use

**Solution:**
1. Change port in `.env` files:
   - Collector: `SERVER_PORT=8082`
   - Demo: `SERVER_PORT=8083`
2. Update Dashboard `.env.local`: `NEXT_PUBLIC_API_URL=http://localhost:8082`

### Problem: Dashboard shows no alerts

**Solution:**
1. Call the slow/error APIs first: http://localhost:8081/api/demo/slow
2. Wait 10 seconds for dashboard auto-refresh
3. Or manually refresh page (F5)

---

## 📚 API Endpoints Reference

### Demo Service (http://localhost:8081)
- `GET /api/demo/success` - Returns 200, fast response
- `GET /api/demo/slow` - Returns 200, 850ms delay (triggers alert)
- `GET /api/demo/error` - Returns 500 error (triggers alert)
- `GET /api/demo/health` - Health check

### Collector Service (http://localhost:8080)
- `POST /api/auth/login` - Login (public)
- `GET /api/logs` - Get all logs (requires JWT)
- `GET /api/logs/slow` - Get slow APIs (requires JWT)
- `GET /api/alerts` - Get all alerts (requires JWT)
- `GET /api/alerts/stats` - Dashboard statistics (requires JWT)

---

## 🔒 Security Note

**⚠️ For Development Only:**
- Don't commit `.env` files to GitHub (they're in .gitignore)
- MongoDB Network Access set to 0.0.0.0/0 is only for development
- Change JWT_SECRET for production use
- Use environment-specific credentials

---

## 📧 Need Help?

If you're stuck:
1. ✅ Check all 3 services are running
2. ✅ Verify MongoDB Atlas connection in Atlas dashboard
3. ✅ Check `.env` files have correct credentials
4. ✅ Look at terminal logs for error messages
5. ✅ Check browser console (F12) for frontend errors

---

## 🎉 You're All Set!

Once everything is running:
- Dashboard: http://localhost:3000
- Demo APIs: http://localhost:8081/api/demo/*
- Collector: http://localhost:8080

**Default Login:** admin / admin123

**Happy Monitoring! 🚀**