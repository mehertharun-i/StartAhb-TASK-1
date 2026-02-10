# 🚀 Quick Start Guide

## 5-Minute Setup

### Step 1: Start Backend (Java)
```bash
cd Task-1
mvn clean install
mvn spring-boot:run
```
✅ Backend runs on `http://localhost:8001`

### Step 2: Start Frontend (Angular)
In a new terminal:
```bash
cd StartAhbTASK1FE
npm install  # Only first time
npm start
```
✅ Frontend runs on `http://localhost:4200`

### Step 3: Test Authentication
1. Open browser: `http://localhost:4200`
2. Click "Create one" to sign up OR use existing account
3. Enter credentials:
   - **Username**: testuser (or any username)
   - **Password**: password123 (or any password)
4. Log in successfully → See dashboard
5. Check token in DevTools → Application → Local Storage

---

## 📋 What's Implemented

### ✅ Frontend (Angular 17)
- **Login Page** - Beautiful form with validation
- **Signup Page** - Multi-field registration
- **Home Dashboard** - Token status & features info
- **JWT Storage** - Automatic localStorage management
- **Auto-Refresh** - Tokens refresh every 60 seconds
- **HTTP Interceptor** - Automatic Bearer token attachment
- **Route Guard** - Protected /home route
- **Logout** - Clear session & redirect
- **Modern UI** - Purple/blue gradient theme, minimal animations

### ✅ Backend (Java Spring Boot)
- **Auth Controller** - `/api/auth/login`, `/api/auth/signup`, `/api/auth/refresh`
- **JWT Service** - Token generation & validation
- **Security Config** - Spring Security setup with JWT filter
- **User Management** - Create, read, update, delete users
- **Password Hashing** - BCrypt encryption
- **Token Expiry** - 60 seconds access, 7 days refresh

### ✅ Features
- 🔐 JWT-based stateless authentication
- 🔄 Automatic token refresh (before expiry)
- 💾 LocalStorage token persistence
- 🎨 Beautiful gradient UI (purple/blue)
- 📱 Responsive design (mobile, tablet, desktop)
- ⚡ Minimal animations (smooth, not distracting)
- ✔️ Form validation (username, password)
- 📊 Status cards on dashboard
- 🚪 Clean logout functionality
- 🛡️ Route protection with authGuard

---

## 🎯 Key Features Explained

### Feature 1: Token Auto-Refresh
Your token expires every **60 seconds**. The system automatically refreshes it at **50 seconds**, so you never get logged out!

**How it works:**
- User logs in → Token stored with expiry time
- Service calculates when to refresh (50 seconds after login)
- Timer set in background
- At 50 seconds, automatic refresh occurs
- New tokens returned, old timer cleared
- Process repeats

**Result:** User can stay logged in indefinitely without manual refresh!

### Feature 2: JWT Token Management
- **Access Token**: Short-lived (60s) for API requests
- **Refresh Token**: Long-lived (7 days) for getting new access tokens
- **Storage**: Browser localStorage (can be secured with httpOnly cookies)
- **Expiry Check**: Frontend validates token before use

### Feature 3: HTTP Interceptor
Every API request automatically:
1. Gets token from localStorage
2. Adds `Authorization: Bearer {token}` header
3. Sends request
4. If 401 response → Automatically refreshes token & retries request
5. User never sees 401 error!

### Feature 4: Beautiful UI
- **Color Scheme**: Purple (#667eea) → Blue (#764ba2) gradient
- **Cards**: White backgrounds with subtle shadows
- **Buttons**: Gradient backgrounds with hover effects
- **Forms**: Clean inputs with validation messages
- **Animations**: Smooth slides, bounces, and spins
- **Responsive**: Works on mobile, tablet, desktop

---

## 📁 File Structure Overview

### Important Frontend Files
```
src/app/
├── core/
│   ├── guards/auth.guard.ts              # Route protection
│   ├── interceptors/auth.interceptor.ts  # Token handling
│   └── services/auth.service.ts          # Auth logic
├── pages/
│   ├── login/                            # Login page
│   ├── signup/                           # Registration page
│   └── home/                             # Dashboard
└── app.routes.ts                         # Route config
```

### Important Backend Files
```
src/main/java/com/task1/
├── controller/AuthController.java        # Auth endpoints
├── security/JWTService.java              # Token generation
├── security/SecurityConfig.java          # Security setup
├── service/UserService.java              # User logic
└── entity/User.java                      # User model
```

---

## 🔑 Common Tasks

### How to Change Token Expiry Time?

**Edit** `Task-1/src/main/resources/application.properties`:
```properties
jwt.expirationTime=120000   # Change to 120 seconds (2 minutes)
```

**Edit** `StartAhbTASK1FE/src/app/core/services/auth.service.ts`:
```typescript
private refreshInterval = 110000; // Refresh at 1:50 (adjust based on expiry)
```

### How to Change Colors?

**Edit** `StartAhbTASK1FE/src/app/pages/login/login.component.css`:
```css
.auth-card {
  background: linear-gradient(135deg, #YOUR_COLOR_1 0%, #YOUR_COLOR_2 100%);
}
```

### How to Add More Form Fields?

1. Update `signup.component.ts` - Add FormControl
2. Update `signup.component.html` - Add input field
3. Update `SignupComponent` class - Add validator
4. Update `AuthController.signup()` - Accept new fields
5. Update `User.java` entity - Add property

---

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Port 4200 already in use | Kill process: `lsof -i :4200` → `kill -9 PID` |
| Port 8001 already in use | Change in `application.properties`: `server.port=8002` |
| CORS error | Check `SecurityConfig.java` has `.cors(cors -> cors.disable())` |
| Token not refreshing | Check browser console for refresh errors |
| Blank login page | Run `npm install` in frontend folder |
| Cannot connect to database | Verify MySQL is running and credentials match |
| Invalid JWT secret | Ensure same secret in `application.properties` |

---

## 🔍 How to Debug

### Check Token in Browser
1. Open DevTools (F12)
2. Go to **Application** tab
3. Click **Local Storage**
4. Look for `access_token` and `refresh_token`
5. Paste token at [jwt.io](https://jwt.io) to decode

### Check Network Requests
1. Open DevTools → **Network** tab
2. Make a login request
3. Click the request in Network tab
4. Check **Headers** tab for `Authorization: Bearer ...`
5. Check **Response** for token data

### Check Console Logs
1. Open DevTools → **Console** tab
2. Look for messages: `Token refreshed successfully`
3. Check for error messages
4. Backend logs visible in terminal where you ran `mvn spring-boot:run`

---

## ✨ Next Steps

### Enhancements You Can Add
1. **OAuth2 Login** - Google, GitHub authentication
2. **Two-Factor Authentication** - SMS/Email verification
3. **User Profile Page** - Edit user details
4. **Password Reset** - Forgot password flow
5. **Remember Me** - Extended session option
6. **Session Management** - See active sessions
7. **Audit Logging** - Track login history
8. **Role-Based Access** - Admin/User roles
9. **API Rate Limiting** - Prevent abuse
10. **Dark Mode** - Theme switcher

### Security Improvements
- [ ] Use httpOnly cookies instead of localStorage
- [ ] Add CSRF token to forms
- [ ] Implement rate limiting on auth endpoints
- [ ] Add stronger password requirements
- [ ] Enable HTTPS for production
- [ ] Add input sanitization
- [ ] Implement account lockout after failed logins
- [ ] Add email verification on signup

---

## 📚 Learning Resources

### Angular Topics Covered
- Standalone components
- Reactive forms with FormGroup
- Angular signals
- Route guards
- HTTP interceptors
- Services with Observables
- Request/response handling
- LocalStorage API

### Java/Spring Topics Covered
- Spring Boot configuration
- Spring Security
- JWT creation & validation
- Password hashing with BCrypt
- REST controller endpoints
- Request/response DTOs
- Database integration with JPA
- Dependency injection

---

## 🎓 Educational Value

This project teaches:
1. **Full-stack Development** - Frontend + Backend
2. **Authentication** - JWT implementation
3. **Modern Web Architecture** - REST APIs
4. **Security** - Token validation, password hashing
5. **UI/UX** - Beautiful, responsive design
6. **State Management** - Services, BehaviorSubjects
7. **Reactive Programming** - RxJS, Observables
8. **Database Integration** - MySQL, JPA/Hibernate

---

## 💡 Pro Tips

✅ **Tip 1**: Browser DevTools has a Network tab - watch ALL requests!
✅ **Tip 2**: JWT tokens can be decoded at [jwt.io](https://jwt.io) - see what's inside!
✅ **Tip 3**: localStorage is visible in DevTools - inspect your tokens!
✅ **Tip 4**: Check both browser console AND backend terminal for errors!
✅ **Tip 5**: Tokens contain expiration time - decode to see when they expire!

---

## 🎉 You're Ready!

Everything is set up and ready to go. Just:

```bash
# Terminal 1: Start Backend
cd Task-1 && mvn spring-boot:run

# Terminal 2: Start Frontend
cd StartAhbTASK1FE && npm start

# Open browser
http://localhost:4200
```

**Signup → Login → See Dashboard → Check Dev Tools for Tokens!**

---

## 📞 Getting Help

- Check the [COMPLETE_PROJECT_GUIDE.md](COMPLETE_PROJECT_GUIDE.md) for detailed docs
- Review [IMPLEMENTATION_DETAILS.md](IMPLEMENTATION_DETAILS.md) for architecture
- Check browser console (F12) for JavaScript errors
- Check backend terminal output for Java exceptions
- Decode tokens at [jwt.io](https://jwt.io)

---

**Happy Coding! 🚀**
