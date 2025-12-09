# 🔐 Environment Variables Setup Checklist

Use this checklist to ensure all `.env` files are configured correctly.

---

## ✅ Pre-Setup: MongoDB Atlas

Before configuring `.env` files, complete these steps:

- [ ] MongoDB Atlas account created
- [ ] Free M0 cluster created
- [ ] Database user created with username and password
- [ ] Network Access set to 0.0.0.0/0 (or your IP)
- [ ] Two databases created: `monitoring_logs` and `monitoring_metadata`
- [ ] Connection string copied (format: `mongodb+srv://user:pass@cluster.mongodb.net/`)

---

## 📁 File 1: `collector-service/.env`

```bash
cd collector-service
cp .env.example .env
```

**Edit `.env` and replace:**

```env
# ⚠️ REPLACE THESE WITH YOUR MONGODB ATLAS CREDENTIALS
LOGS_MONGO_URI=mongodb+srv://YOUR_USER:YOUR_PASS@YOUR_CLUSTER.mongodb.net/monitoring_logs?retryWrites=true&w=majority
METADATA_MONGO_URI=mongodb+srv://YOUR_USER:YOUR_PASS@YOUR_CLUSTER.mongodb.net/monitoring_metadata?retryWrites=true&w=majority

# Optional: Change for production
JWT_SECRET=myVerySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256Algorithm
JWT_EXPIRATION=86400000

# Optional: Change if port 8080 is in use
SERVER_PORT=8080
```

**Example with real values:**
```env
LOGS_MONGO_URI=mongodb+srv://project_user:ProjectPass123@api-monitoring-cluster.88wlsdc.mongodb.net/monitoring_logs?retryWrites=true&w=majority
METADATA_MONGO_URI=mongodb+srv://project_user:ProjectPass123@api-monitoring-cluster.88wlsdc.mongodb.net/monitoring_metadata?retryWrites=true&w=majority
JWT_SECRET=myVerySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256Algorithm
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

**Checklist:**
- [ ] `.env` file exists in `collector-service/` folder
- [ ] `YOUR_USER` replaced with MongoDB username
- [ ] `YOUR_PASS` replaced with MongoDB password
- [ ] `YOUR_CLUSTER` replaced with cluster address
- [ ] `/monitoring_logs` present in first URI
- [ ] `/monitoring_metadata` present in second URI
- [ ] No spaces before/after `=` signs
- [ ] File saved

---

## 📁 File 2: `demo-monitoring-service/.env`

```bash
cd demo-monitoring-service
cp .env.example .env
```

**Usually no changes needed:**

```env
SERVER_PORT=8081
SPRING_APPLICATION_NAME=demo-service
COLLECTOR_URL=http://localhost:8080/api/collector
RATE_LIMIT_SERVICE=demo-service
RATE_LIMIT_VALUE=100
RATE_LIMIT_ENABLED=true
```

**Only change if:**
- Collector is on different port → Update `COLLECTOR_URL`
- Port 8081 is in use → Change `SERVER_PORT`
- Want different rate limit → Change `RATE_LIMIT_VALUE`

**Checklist:**
- [ ] `.env` file exists in `demo-monitoring-service/` folder
- [ ] `COLLECTOR_URL` points to correct Collector port
- [ ] File saved

---

## 📁 File 3: `dashboard-ui/.env.local`

```bash
cd dashboard-ui
cp .env.example .env.local
```

**Usually no changes needed:**

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

**Only change if:**
- Collector is on different port → Update to `http://localhost:YOUR_PORT`

**Checklist:**
- [ ] `.env.local` file exists in `dashboard-ui/` folder
- [ ] `NEXT_PUBLIC_API_URL` points to Collector Service
- [ ] File saved

---

## 🧪 Verification

After configuring all `.env` files:

### 1. Test Collector Service

```bash
cd collector-service
mvnw.cmd spring-boot:run
```

**Expected output:**
```
✅ Default user created: username=admin, password=admin123
Started CollectorServiceApplication in X.XXX seconds
```

**❌ If errors:**
- `MongoTimeoutException` → Check Network Access in Atlas
- `Could not resolve placeholder` → Check `.env` file exists and has correct variables
- `Authentication failed` → Check username/password in `.env`

### 2. Test Demo Service

```bash
cd demo-monitoring-service
mvnw.cmd spring-boot:run
```

**Expected output:**
```
🚀 Demo Monitoring Service Started on port 8081
```

### 3. Test Dashboard

```bash
cd dashboard-ui
npm run dev
```

**Expected output:**
```
✓ Ready in 2.5s
○ Local:   http://localhost:3000
```

---

## 🚨 Common Mistakes

### ❌ Wrong URI Format

**Wrong:**
```env
LOGS_MONGO_URI=mongodb+srv://user:pass@cluster.mongodb.net/?retryWrites=true
```

**Correct:**
```env
LOGS_MONGO_URI=mongodb+srv://user:pass@cluster.mongodb.net/monitoring_logs?retryWrites=true&w=majority
```

Notice `/monitoring_logs` BEFORE the `?`

### ❌ Special Characters in Password

If your password has special characters like `@`, `#`, `!`, you must URL-encode them:

- `@` → `%40`
- `#` → `%23`
- `!` → `%21`

**Example:**
```
Password: Pass@123
Encoded: Pass%40123
URI: mongodb+srv://user:Pass%40123@cluster.mongodb.net/...
```

### ❌ Spaces in .env File

**Wrong:**
```env
LOGS_MONGO_URI = mongodb+srv://...
```

**Correct:**
```env
LOGS_MONGO_URI=mongodb+srv://...
```

No spaces before/after `=`

### ❌ Missing Database Name

**Wrong:**
```env
LOGS_MONGO_URI=mongodb+srv://user:pass@cluster.mongodb.net/?retryWrites=true
```

**Correct:**
```env
LOGS_MONGO_URI=mongodb+srv://user:pass@cluster.mongodb.net/monitoring_logs?retryWrites=true&w=majority
```

---

## ✅ Final Checklist

Before running the application:

- [ ] All 3 `.env` files created from `.env.example`
- [ ] MongoDB credentials added to `collector-service/.env`
- [ ] All services start without errors
- [ ] Can login to dashboard at http://localhost:3000
- [ ] Test APIs work and create logs

---

## 🆘 Need Help?

If setup fails:

1. **Check MongoDB Atlas:**
   - Network Access allows your IP
   - Database user has read/write permissions
   - Databases exist: `monitoring_logs` and `monitoring_metadata`

2. **Check `.env` files:**
   - No typos in variable names
   - No spaces around `=`
   - Database names in URIs
   - Special characters URL-encoded

3. **Check terminal logs:**
   - Read error messages carefully
   - Look for "MongoTimeoutException", "Authentication failed", etc.

4. **Still stuck?**
   - Read [SETUP.md](SETUP.md#-troubleshooting) for detailed troubleshooting
   - Check if services are running: `netstat -ano | findstr :8080`

---

**Once all checkboxes are ✅, you're ready to run the application!** 🚀