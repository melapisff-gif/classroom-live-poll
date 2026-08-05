# 🔲 MANUAL TASKS - Tumhe Ye Karna Hai

## STATUS CHECKLIST

### Step 1: GitHub Push
- [ ] GitHub pe naya repo banao: `classroom-live-poll`
- [ ] Terminal kholo aur ye commands chalao:

```bash
cd D:\vidly\Vidly Poll\classroom-live-poll
git init
git add .
git commit -m "Classroom Live Poll System - Full Stack"
git branch -M main
git remote add origin https://github.com/TUMAH_USERNAME/classroom-live-poll.git
git push -u origin main
```

---

### Step 2: Render Deploy
- [ ] https://render.com pe jao
- [ ] Sign up / Login karo
- [ ] **New +** button pe click karo
- [ ] **Web Service** select karo
- [ ] **GitHub** connect karo
- [ ] Apna `classroom-live-poll` repo select karo

#### Render Settings Fill Karo:
| Field | Value | Copy-Paste |
|-------|-------|------------|
| Name | `classroom-poll-backend` | `classroom-poll-backend` |
| Region | `Oregon (US West)` | - |
| Branch | `main` | - |
| Runtime | `Node` | - |
| Build Command | `npm ci && npx prisma generate` | `npm ci && npx prisma generate` |
| Start Command | `node src/server.js` | `node src/server.js` |
| Plan | `Free` | - |

- [ ] **Advanced** pe click karo
- [ ] **Add Environment Variable** pe click karo

---

### Step 3: Environment Variables Add Karo

Render Environment tab mein ye variables add karo:

| # | KEY | VALUE |
|---|-----|-------|
| 1 | `DATABASE_URL` | `postgresql://postgres:smapp1234%403A@db.dqoztwzcmfhpsjxipexg.supabase.co:5432/postgres` |
| 2 | `JWT_SECRET` | `classroom_poll_secret_key_2024` |
| 3 | `JWT_REFRESH_SECRET` | `classroom_poll_refresh_key_2024` |
| 4 | `JWT_EXPIRES_IN` | `15m` |
| 5 | `JWT_REFRESH_EXPIRES_IN` | `7d` |
| 6 | `CORS_ORIGIN` | `*` |
| 7 | `NODE_ENV` | `production` |

- [ ] Saare variables add kar diye?
- [ ] **Create Web Service** button pe click karo

---

### Step 4: Deploy Wait Karo
- [ ] Render build shuru karega (2-5 minute lagega)
- [ ] Build logs check karo koi error toh nahi
- [ ] Deploy hone do
- [ ] URL milega: `https://classroom-poll-backend.onrender.com`

---

### Step 5: URL Test Karo
Browser mein ye URL open karo:
```
https://classroom-poll-backend.onrender.com/health
```

Expected response:
```json
{"status":"ok","timestamp":"2026-..."}
```

- [ ] Health check response aaya?

---

### Step 6: mujhe URL Batao
- [ ] Render ka URL mujhe batao (jaise `https://classroom-poll-backend.onrender.com`)
- [ ] Main NetworkModule.kt mein URL update kar dunga

---

## TROUBLESHOOTING

### Agar Build Fail ho:
1. Logs check karo
2. Supabase database chalu hai?
3. Password sahi hai?

### Agar Deploy baad mein 503 Error aaye:
Render free tier pe 15 min inactive ho jata hai. First request slow aayegi.

### Agar Database Connection Error aaye:
Supabase dashboard pe jao → Settings → Database → IP Restriction off karo

---

## QUICK REFERENCE

### Render Dashboard URL:
```
https://dashboard.render.com
```

### Supabase Dashboard URL:
```
https://supabase.com/dashboard
```

### API Test URL:
```
https://classroom-poll-backend.onrender.com/api-docs
```

### Demo Login:
```
Email: teacher@demo.com
Password: password123
```

---

## DONE INDICATORS

Sab ho gaya jab:
- [ ] GitHub pe code hai
- [ ] Render pe deploy hai
- [ ] Health check kaam kar raha hai
- [ ] API docs open ho rahe hain
- [ ] Login test ho gaya

**Tab mujhe bolo, main Teacher App ka BASE_URL update karunga!** ✅
