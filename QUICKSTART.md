# ⚡ 5-Minute Quick Start

Get the project running in 5 minutes (assuming you already have Java and Node.js installed).

---

## Step 1: Clone (30 seconds)

```bash
git clone https://github.com/YOUR_USERNAME/api-monitoring-platform.git
cd api-monitoring-platform
```

---

## Step 2: MongoDB Atlas (2 minutes)

1. Go to [cloud.mongodb.com](https://cloud.mongodb.com/)
2. Sign up/login → Create **FREE cluster**
3. **Database Access** → Add user → Save username/password
4. **Network Access** → Add IP → Choose "Allow from anywhere (0.0.0.0/0)"
5. **Database** → Browse Collections → Create databases:
   - `monitoring_logs`
   - `monitoring_metadata`
6. Click **Connect** → Copy connection string

---

## Step 3: Configure .env Files (1 minute)

### Collector Service:
```bash
cd collector-service
cp .env.example .env
```

Edit `.env` - Replace with YOUR credentials:
```env
LOGS_MONGO_URI=mongodb+srv://YOUR_USER:YOUR_PASS@YOUR_CLUSTER.mongodb.net/monitoring_logs?retryWrites=true&w=majority
METADATA_MONGO_URI=mongodb+srv://YOUR_USER:YOUR_PASS@YOUR_CLUSTER.mongodb.net/monitoring_metadata?retryWrites=true&w=majority
```

### Demo Service:
```bash
cd ../demo-monitoring-service
cp .env.example .env
# No changes needed - just copy!
```

### Dashboard:
```bash
cd ../dashboard-ui
cp .env.example .env.local
# No changes needed - just copy!
```

---

## Step 4: Install & Run (2 minutes)

Open **3 terminals**:

**Terminal 1:** Collector
```bash
cd collector-service
mvnw.cmd clean install -DskipTests && mvnw.cmd spring-boot:run
```

**Terminal 2:** Demo Service
```bash
cd demo-monitoring-service
mvnw.cmd clean install -DskipTests && mvnw.cmd spring-boot:run
```

**Terminal 3:** Dashboard
```bash
cd dashboard-ui
npm install && npm run dev
```

---

## Step 5: Test It!

1. Open: **http://localhost:3000**
2. Login: `admin` / `admin123`
3. Call APIs:
   - http://localhost:8081/api/demo/success
   - http://localhost:8081/api/demo/slow
   - http://localhost:8081/api/demo/error
4. Refresh dashboard → See data!

---

## 🎉 Done!

You should see:
- ✅ Dashboard with stats
- ✅ Logs in table
- ✅ Alerts appearing

---

## ❌ If Something Breaks

**Collector won't start?**
→ Check MongoDB credentials in `collector-service/.env`

**Dashboard shows no data?**
→ Make sure Collector is running first

**Need detailed help?**
→ Read [SETUP.md](SETUP.md)

---

**That's it! 🚀**