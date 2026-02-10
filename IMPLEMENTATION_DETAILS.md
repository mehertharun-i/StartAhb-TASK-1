# Implementation Details & Architecture

## 🏗️ Architecture Overview

### Backend Architecture (Spring Boot)

```
┌─────────────────────────────────────────────────────┐
│                   Client (Angular)                   │
└──────────────────┬──────────────────────────────────┘
                   │ HTTP/REST
┌──────────────────▼──────────────────────────────────┐
│            Spring Security Filter Chain             │
│  ┌───────────────────────────────────────────────┐  │
│  │   JWTAuthFilter (Extract & Validate Token)    │  │
│  └───────────────────────────────────────────────┘  │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│           Controllers & Route Handlers              │
│  ┌───────────────────────────────────────────────┐  │
│  │   AuthController:                             │  │
│  │   - POST /api/auth/login                      │  │
│  │   - POST /api/auth/signup                     │  │
│  │   - POST /api/auth/refresh                    │  │
│  └───────────────────────────────────────────────┘  │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│         Services (Business Logic Layer)             │
│  ┌───────────────────────────────────────────────┐  │
│  │   UserService                                 │  │
│  │   JWTService                                  │  │
│  └───────────────────────────────────────────────┘  │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│       Repository/DAO Layer (Database Access)        │
│  ┌───────────────────────────────────────────────┐  │
│  │   UserRepository (Spring Data JPA)            │  │
│  └───────────────────────────────────────────────┘  │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│              MySQL Database                         │
├──────────────────────────────────────────────────────┤
│   users table:                                       │
│   - user_id (PK)                                     │
│   - user_login_id                                    │
│   - user_password (hashed)                           │
│   - user_first_name                                  │
│   - user_last_name                                   │
│   - created_at                                       │
└──────────────────────────────────────────────────────┘
```

### Frontend Architecture (Angular)

```
┌──────────────────────────────────────────────────────┐
│                  app.routes.ts                       │
│  ┌────────────────────────────────────────────────┐  │
│  │   /login → LoginComponent                      │  │
│  │   /signup → SignupComponent                    │  │
│  │   /home → HomeComponent (guarded)              │  │
│  └────────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────┐
│           Core Guard & Interceptors                  │
│  ┌────────────────────────────────────────────────┐  │
│  │   authGuard (Route Protection)                 │  │
│  │   AuthInterceptor (Token Management)           │  │
│  └────────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────┐
│              AuthService                             │
│  ┌────────────────────────────────────────────────┐  │
│  │   login()        - Send credentials            │  │
│  │   signup()       - Register user               │  │
│  │   logout()       - Clear tokens                │  │
│  │   refreshToken() - Get new access token        │  │
│  │   scheduleRefresh() - Auto-refresh timer       │  │
│  └────────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────┐
│        HttpClientModule (HTTP Requests)              │
└──────────────────┬───────────────────────────────────┘
                   │ HTTP/REST
                   │
              Backend API
```

---

## 🔄 Token Flow Diagram

### Initial Login
```
User Input (Username, Password)
         ↓
POST /api/auth/login
         ↓
Backend Verification
         ↓
JWT Generation (access + refresh)
         ↓
Response: { accessToken, refreshToken, expiresIn }
         ↓
Store in localStorage
         ↓
Schedule Auto-Refresh (at 50s)
         ↓
Navigate to /home
```

### Auto Token Refresh
```
50 seconds after login...
         ↓
Schedule refresh timeout triggers
         ↓
POST /api/auth/refresh (with Bearer token)
         ↓
Backend validates current token
         ↓
New tokens generated
         ↓
Response: { accessToken, refreshToken, expiresIn }
         ↓
Update localStorage
         ↓
Reschedule next refresh
         ↓
User never logged out!
```

### Protected API Request
```
User triggers action (e.g., fetch data)
         ↓
HTTP Request created
         ↓
AuthInterceptor intercepts request
         ↓
Get token from localStorage
         ↓
Add header: Authorization: Bearer {token}
         ↓
Send request to backend
         ↓
Backend validates JWT
         ↓
If valid → Process request → Return data ✓
If invalid (401) → AuthInterceptor handles refresh
         ↓
Retry original request with new token
```

---

## 💾 Data Models

### Authentication Response
```typescript
interface AuthResponse {
  accessToken: string;        // JWT token (60s expiry)
  refreshToken: string;       // Refresh token (7 days)
  expiresIn: number;          // Milliseconds until expiry
  tokenType: string;          // Always "Bearer"
  message: string;            // Status message
}
```

### JWT Payload (Decoded)
```typescript
interface JwtPayload {
  sub: string;                // Subject (username)
  iat: number;                // Issued at (timestamp)
  exp: number;                // Expiration (timestamp)
  [key: string]: any;         // Additional claims
}
```

### User DTO
```typescript
interface UserRequestDto {
  userFirstName: string;
  userLastName: string;
  userLoginId: string;
  userPassword: string;
}

interface UserResponseDto {
  userId: number;
  userFirstName: string;
  userLastName: string;
  userLoginId: string;
  // password excluded
}
```

---

## 🔐 Security Implementation

### Password Security
- **Algorithm**: BCrypt with salt rounds
- **Hashing Location**: UserController.userCreate()
- **Verification**: Spring Security's AuthenticationManager

### JWT Security
- **Secret Key**: BASE64 encoded (256-bit)
- **Algorithm**: HS256 (HMAC with SHA-256)
- **Validation**: 
  - Signature verification
  - Expiry check
  - Subject validation

### Interceptor Chain
1. **JWTAuthFilter** - Extracts & validates token
2. **UsernamePasswordAuthenticationFilter** - Only for login
3. **AuthorizationFilters** - Path-based access control

---

## 🎯 State Management Strategy

### Service-Based BehaviorSubjects
```typescript
// In AuthService
public isLoggedIn$ = new BehaviorSubject<boolean>(...);
public currentUser$ = new BehaviorSubject<string | null>(...);

// Used in components
this.auth.isLoggedIn$.subscribe(loggedIn => {
  // Update template
});
```

### Reactive Forms with Signals
```typescript
// In LoginComponent
loading = signal(false);
error = signal<string | null>(null);
showPassword = signal(false);

// Update signals
this.loading.set(true);
this.error.set('Invalid credentials');
```

---

## 📱 Responsive Design

### Breakpoints
```css
/* Desktop */
@media (min-width: 1200px) { /* ... */ }

/* Tablet */
@media (max-width: 768px) { /* ... */ }

/* Mobile */
@media (max-width: 480px) { /* ... */ }
```

### Mobile Optimizations
- Flexible grid layouts
- Touch-friendly button sizes (min 44px)
- Simplified navigation
- Font size scaling
- Image optimization

---

## ⚡ Performance Considerations

### Frontend Optimizations
- ✅ Standalone components (no module overhead)
- ✅ OnPush change detection
- ✅ Lazy loading routes
- ✅ Tree-shaking enabled
- ✅ AOT compilation

### Backend Optimizations
- ✅ JWT stateless (no session storage)
- ✅ Spring Boot embedded server
- ✅ MySQL connection pooling
- ✅ Indexed user lookups
- ✅ BCrypt iterations tuned

### Network Optimization
- ✅ Interceptor reuses connections
- ✅ Token attached to all requests
- ✅ Automatic refresh before expiry (no 401 retries)

---

## 🧪 Test Scenarios

### Happy Path
```
1. User signs up
   POST /api/auth/signup
   Returns: accessToken, refreshToken

2. User logs in after signup
   POST /api/auth/login
   Returns: accessToken, refreshToken

3. User stays on app for 60 seconds
   Auto-refresh at 50 seconds
   New tokens received silently
   User unaware of refresh

4. User makes API call
   AuthInterceptor adds Authorization header
   Backend validates JWT
   Returns user data

5. User logs out
   localStorage cleared
   Navigate to /login
   New session required
```

### Edge Cases
```
1. User opens new tab
   Both tabs have same token in localStorage
   Both can make requests (no token invalidation)

2. User closes browser immediately
   Auto-refresh timer unreachable
   Next login gets new tokens

3. User token expires while offline
   Next request gets 401
   Refresh fails (new login required)

4. Multiple rapid API calls
   Interceptor queues if refresh in progress
   All use same refreshed token

5. Malformed token in localStorage
   Auth service detects invalid JWT
   User redirected to login
```

---

## 🔧 Customization Guide

### Change Token Expiry
**Backend** (`application.properties`):
```properties
jwt.expirationTime=120000  # 2 minutes
```

**Frontend** (`auth.service.ts`):
```typescript
private refreshInterval = 110000; // Refresh at 1:50
```

### Change Colors
**Login Component** (`login.component.css`):
```css
background: linear-gradient(135deg, #your_color_1 0%, #your_color_2 100%);
```

### Add Additional User Fields
1. Update `User.java` entity
2. Update UserRequestDto/UserResponseDto
3. Update signup form in `signup.component.ts`
4. Update database schema

### Add OAuth2 Login
1. Add Spring Security OAuth2 dependency
2. Configure OAuth2ClientRegistration
3. Create OAuth2Controller endpoint
4. Add OAuth2 button to login page
5. Handle redirect from OAuth provider

---

## 📊 Database Schema

```sql
CREATE TABLE users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  user_login_id VARCHAR(100) UNIQUE NOT NULL,
  user_password VARCHAR(255) NOT NULL,
  user_first_name VARCHAR(100),
  user_last_name VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_login_id ON users(user_login_id);
...
```

---

## 🚀 Deployment Checklist

### Frontend
- [ ] Build for production: `npm run build`
- [ ] Output in `dist/` folder
- [ ] Deploy to static hosting (Netlify, Vercel, AWS S3)
- [ ] Setup environment variables
- [ ] Configure CORS headers
- [ ] Enable HTTPS

### Backend
- [ ] Update `application.properties` with production DB
- [ ] Change `jwt.secretKey` to strong value
- [ ] Disable `spring.jpa.show-sql=true`
- [ ] Set logging levels appropriately
- [ ] Build JAR: `mvn clean package`
- [ ] Deploy to server (Heroku, AWS, GCP, DigitalOcean)
- [ ] Setup database backups
- [ ] Configure monitoring & logging

---

## 📝 Coding Standards

### TypeScript
- Use strict mode
- Explicit return types
- Avoid `any` type
- Use interfaces over types
- Proper error handling

### Java
- Follow Java conventions
- Use meaningful variable names
- Proper exception handling
- Add JavaDoc comments
- Use dependency injection

### CSS
- BEM methodology for classes
- CSS variables for theme colors
- Mobile-first approach
- Semantic HTML
- Accessibility (WCAG 2.1)

---

## 🔗 API Documentation

### Login Endpoint
```
POST /api/auth/login
Content-Type: application/json

Request:
{
  "userLoginId": "testuser",
  "password": "password123"
}

Response (200):
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "expiresIn": 60000,
  "tokenType": "Bearer",
  "message": "Login successful"
}

Response (401):
{
  "accessToken": null,
  "refreshToken": null,
  "message": "Invalid credentials"
}
```

### Refresh Token Endpoint
```
POST /api/auth/refresh
Authorization: Bearer {accessToken}

Response (200):
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "expiresIn": 60000,
  "tokenType": "Bearer",
  "message": "Token refreshed"
}

Response (401):
{
  "message": "Token refresh failed"
}
```

---

**Last Updated**: February 6, 2026
**Version**: 1.0.0
