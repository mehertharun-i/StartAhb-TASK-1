# Full-Stack JWT Authentication with Angular 17 & Java Spring Boot

A complete authentication system featuring JWT token management with automatic refresh capabilities, clean modern UI, and REST API integration.

---

## 📁 Project Structure

```
StartAhb-Task-1/
├── StartAhbTASK1FE/                    # Angular 17 Frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/
│   │   │   │   ├── guards/
│   │   │   │   │   └── auth.guard.ts                 # Route protection guard
│   │   │   │   ├── interceptors/
│   │   │   │   │   └── auth.interceptor.ts           # JWT token attachment & refresh
│   │   │   │   └── services/
│   │   │   │       └── auth.service.ts               # Auth logic, token management
│   │   │   ├── pages/
│   │   │   │   ├── login/
│   │   │   │   │   ├── login.component.ts            # Login form
│   │   │   │   │   ├── login.component.html
│   │   │   │   │   └── login.component.css
│   │   │   │   ├── signup/
│   │   │   │   │   ├── signup.component.ts           # Registration form
│   │   │   │   │   ├── signup.component.html
│   │   │   │   │   └── signup.component.css
│   │   │   │   └── home/
│   │   │   │       ├── home.component.ts             # Dashboard
│   │   │   │       ├── home.component.html
│   │   │   │       └── home.component.css
│   │   │   ├── shared/
│   │   │   │   └── models/
│   │   │   │       └── auth.models.ts
│   │   │   ├── app.ts                                # Root component
│   │   │   ├── app.routes.ts                         # Route configuration
│   │   │   └── app.html
│   │   ├── main.ts                                    # Bootstrap & providers
│   │   └── styles.css                                 # Global styles
│   ├── package.json
│   ├── README.md
│   └── tsconfig.json
│
└── Task-1/                             # Java Spring Boot Backend
    ├── src/main/java/com/task1/
    │   ├── controller/
    │   │   ├── AuthController.java                   # Auth endpoints
    │   │   └── UserController.java                   # User management
    │   ├── service/
    │   │   ├── UserService.java
    │   │   └── UserServiceImpl.java
    │   ├── security/
    │   │   ├── AuthController.java
    │   │   ├── JWTService.java                       # JWT generation & validation
    │   │   ├── JWTAuthFilter.java                    # Token extraction filter
    │   │   ├── SecurityConfig.java                   # Spring Security config
    │   │   └── SecurityUserDetailsFromDB.java
    │   ├── dto/
    │   │   ├── AuthResponse.java                     # Login response DTO
    │   │   ├── LoginDetails.java
    │   │   ├── UserRequestDto.java
    │   │   └── UserResponseDto.java
    │   ├── entity/
    │   │   └── User.java                             # User entity
    │   ├── dao/
    │   │   └── UserRepository.java
    │   └── Task1Application.java
    ├── src/main/resources/
    │   └── application.properties                    # JWT config
    ├── pom.xml
    └── README.md
```

---

## 🔐 Authentication Flow

### Login Flow
1. User enters username & password
2. Frontend sends credentials to `POST /api/auth/login`
3. Backend authenticates and generates JWT tokens
4. Tokens returned: `accessToken` (60s) & `refreshToken` (7 days)
5. Frontend stores tokens in localStorage
6. Auto-refresh scheduled for 10 seconds before expiry

### Token Refresh Flow
1. Token approaching expiry (at 50 seconds after login)
2. Frontend automatically sends refresh request to `POST /api/auth/refresh`
3. New tokens generated immediately
4. User stays logged in seamlessly without logout

### Protected Routes
- All API requests automatically include `Authorization: Bearer {token}` header via interceptor
- 401 responses trigger automatic token refresh
- Failed refresh logs user out

---

## 🎨 UI/UX Features

### Color Scheme
- **Primary Gradient**: Purple (#667eea) → Blue (#764ba2)
- **Background**: Light blue gradient
- **Cards**: Clean white with subtle shadows
- **Accent Colors**: Green (success), Red (error), Blue (info)

### Components
- ✅ **Login Page**: Minimal, focused form with validation
- ✅ **Signup Page**: Multi-field registration form
- ✅ **Home Dashboard**: Welcome page with token status info
- ✅ **Navbar**: User greeting, logout button
- ✅ **Error Alerts**: Inline validation & error messages
- ✅ **Loading States**: Spinner animation during requests

### Animations
- Smooth slide-up entrance (login/signup cards)
- Hover effects on buttons & cards
- Loading spinner on submit buttons
- Subtle scale transform on bounce

---

## 🛠️ Backend Setup

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### Configuration
Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8001

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/StartAhb_TASK_1
spring.datasource.username=root
spring.datasource.password=Meher@147
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secretKey=e+NqwTw+6AR5ZXQcVesb2OWNbaaKmh41CDC8TX88BlM=
jwt.expirationTime=60000              # 60 seconds
jwt.refreshTokenExpiration=604800000  # 7 days
```

### Running Backend
```bash
cd Task-1
mvn clean install
mvn spring-boot:run
# Server runs on http://localhost:8001
```

### API Endpoints
- `POST /api/auth/login` - Login with credentials
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/refresh` - Refresh access token
- `GET /user/{id}` - Get user details (protected)
- `GET /user/all` - List all users (protected)

---

## 🚀 Frontend Setup

### Prerequisites
- Node.js 18+
- npm 9+
- Angular CLI 17+

### Installation
```bash
cd StartAhbTASK1FE
npm install
```

### Configuration
Update API URL in `src/app/core/services/auth.service.ts` if needed:
```typescript
private readonly API = 'http://localhost:8001/api/auth';
```

### Running Frontend
```bash
# Development server
npm start
# Navigate to http://localhost:4200

# Build for production
npm run build
```

### Environment Variables (Optional)
Create `.env` file in project root:
```
API_BASE_URL=http://localhost:8001
```

---

## 📋 Key Implementation Details

### JWT Token Management
- **Expiry**: 60 seconds (short-lived access token)
- **Refresh Token**: 7 days
- **Refresh Timing**: Automatic at 50 seconds (10 seconds before expiry)
- **Storage**: Browser localStorage (not httpOnly for demo)

### Auth Service (`auth.service.ts`)
```typescript
// Auto-scheduling token refresh
scheduleRefresh(): void {
  const token = this.getAccessToken();
  const expiryTime = jwt_decode(token).exp * 1000;
  const timeUntilRefresh = expiryTime - Date.now() - 10000;
  
  setTimeout(() => {
    this.refreshToken().subscribe();
  }, timeUntilRefresh);
}
```

### HTTP Interceptor
- Attaches JWT to `Authorization` header
- Handles 401 responses with automatic refresh
- Queues requests during refresh using BehaviorSubject

### Auth Guard
- Validates token expiry
- Routes to login if unauthorized
- Preserves redirect URL for post-login navigation

---

## 🔒 Security Considerations

### Implemented
✅ JWT-based stateless authentication
✅ Automatic token refresh before expiry
✅ HTTP interceptor for secure requests
✅ Route guards preventing unauthorized access
✅ Password hashing with BCrypt
✅ CORS configuration

### Production Recommendations
⚠️ Use httpOnly cookies instead of localStorage for tokens
⚠️ Implement HTTPS only
⚠️ Add rate limiting on auth endpoints
⚠️ Implement CSRF protection
⚠️ Use stronger JWT secret key
⚠️ Add OAuth2 support (Google, GitHub, etc.)
⚠️ Implement two-factor authentication
⚠️ Add audit logging for auth events

---

## 🧪 Testing

### Test Users
Create test users via signup, or use:
```
Username: testuser
Password: password123
```

### Manual Testing Flow
1. Open http://localhost:4200/login
2. Try signup or login
3. Tokens stored in localStorage (DevTools > Application > Storage)
4. Open home dashboard after login
5. Wait 50+ seconds to see automatic token refresh in console
6. Make API requests to verify Authorization header

---

## 🐛 Troubleshooting

### Frontend Issues
- **"Cannot find module"** → Run `npm install`
- **Port 4200 in use** → Change in `angular.json` or kill process
- **CORS errors** → Check backend CORS config in `SecurityConfig.java`
- **Blank page** → Check browser console for errors

### Backend Issues
- **Connection refused** → Verify MySQL is running
- **Invalid JWT** → Check secret key matches in config
- **401 on refresh** → Ensure refresh endpoint is permitted in SecurityConfig

### Token Issues
- **Token not refreshing** → Check browser console for refresh errors
- **Logged out unexpectedly** → Verify localStorage in DevTools
- **Token expiry mismatch** → Sync backend/frontend time

---

## 📚 Technologies Used

### Frontend
- **Angular 17** - Modern web framework
- **TypeScript** - Type-safe JavaScript
- **RxJS** - Reactive programming
- **jwt-decode** - JWT parsing
- **CSS3** - Modern styling with gradients

### Backend
- **Spring Boot 4.0.2** - Java framework
- **Spring Security** - Authentication/Authorization
- **JPA/Hibernate** - ORM
- **MySQL** - Database
- **JJWT** - JWT library
- **BCrypt** - Password encoding

---

## 📖 Documentation Files

### Angular Component Docs
- [Login Component](src/app/pages/login/README.md)
- [Signup Component](src/app/pages/signup/README.md)
- [Home Component](src/app/pages/home/README.md)
- [Auth Service](src/app/core/services/README.md)

### Java API Docs
- [Auth Controller](../../Task-1/README.md#auth-controller)
- [JWT Service](../../Task-1/README.md#jwt-service)

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

---

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 👤 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- Angular Team for the excellent framework
- Spring Boot community for robust tools
- Material Design for UI inspiration

---

## 📞 Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Email: support@example.com
- Visit documentation: [docs.example.com](https://docs.example.com)

---

**Last Updated**: February 6, 2026
**Version**: 1.0.0
