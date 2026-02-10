## Backend REST API Endpoints Documentation

This document outlines all REST API endpoints from the backend (Task-1 project) and the corresponding frontend implementation.

---

## Authentication Endpoints (AuthController)

**Base URL:** `http://localhost:8001/api/auth`
**CORS:** Enabled for all origins (*)

### 1. User Login
- **Method:** `POST`
- **Endpoint:** `/api/auth/login`
- **Request Body:**
  ```json
  {
    "userLoginId": "string",
    "password": "string"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "accessToken": "string (JWT)",
    "refreshToken": "string",
    "expiresIn": 60000,
    "tokenType": "Bearer",
    "message": "Login successful"
  }
  ```
- **Error Response (401 Unauthorized):**
  ```json
  {
    "accessToken": null,
    "refreshToken": null,
    "expiresIn": 0,
    "tokenType": null,
    "message": "Invalid credentials"
  }
  ```
- **Frontend Implementation:** [auth.service.ts](../src/app/core/services/auth.service.ts) - `login()` method

---

### 2. User Signup
- **Method:** `POST`
- **Endpoint:** `/api/auth/signup`
- **Request Body:**
  ```json
  {
    "userFirstName": "string",
    "userLastName": "string",
    "userEmail": "string (email format)",
    "userPhoneNumber": "string (10+ digits)",
    "userDateOfBirth": "string (YYYY-MM-DD format)",
    "userLoginId": "string",
    "userPassword": "string",
    "addressClass": [
      {
        "street": "string",
        "city": "string",
        "state": "string",
        "postalCode": "string",
        "country": "string"
      }
    ]
  }
  ```
- **Response (201 Created):**
  ```json
  {
    "accessToken": "string (JWT)",
    "refreshToken": "string",
    "expiresIn": 60000,
    "tokenType": "Bearer",
    "message": "Signup successful"
  }
  ```
- **Error Response (400 Bad Request):**
  ```json
  {
    "accessToken": null,
    "refreshToken": null,
    "expiresIn": 0,
    "tokenType": null,
    "message": "Signup failed: <error details>"
  }
  ```
- **Frontend Implementation:** [signup.component.ts](../src/app/pages/signup/signup.component.ts) - `submit()` method
- **Form Fields:** 
  - First Name (required, min 2 chars)
  - Last Name (required, min 2 chars)
  - Email (required, valid email format)
  - Phone Number (required, min 10 digits)
  - Date of Birth (required, date picker)
  - Username (required, min 3 chars)
  - Password (required, min 6 chars)
  - Confirm Password (required, must match password)

---

### 3. Refresh Token
- **Method:** `POST`
- **Endpoint:** `/api/auth/refresh`
- **Request Headers:**
  ```
  Authorization: Bearer <accessToken>
  ```
- **Request Body:** Empty (no body required)
- **Response (200 OK):**
  ```json
  {
    "accessToken": "string (new JWT)",
    "refreshToken": "string (new refresh token)",
    "expiresIn": 60000,
    "tokenType": "Bearer",
    "message": "Token refreshed"
  }
  ```
- **Error Response (401 Unauthorized):**
  ```json
  {
    "accessToken": null,
    "refreshToken": null,
    "expiresIn": 0,
    "tokenType": null,
    "message": "Token refresh failed"
  }
  ```
- **Frontend Implementation:** [auth.service.ts](../src/app/core/services/auth.service.ts) - `refreshToken()` method
- **Auto-Refresh:** Tokens are automatically refreshed 10 seconds before expiration using `scheduleRefresh()` method

---

## User Management Endpoints (UserController)

**Base URL:** `http://localhost:8001/user`
**Authorization:** Requires valid JWT token in Authorization header

### 1. Create User
- **Method:** `POST`
- **Endpoint:** `/user/create`
- **Request Body:**
  ```json
  {
    "userFirstName": "string",
    "userLastName": "string",
    "userEmail": "string",
    "userPhoneNumber": "string",
    "userDateOfBirth": "string (YYYY-MM-DD)",
    "userLoginId": "string",
    "userPassword": "string",
    "addressClass": []
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "userId": 1,
    "userFirstName": "string",
    "userLastName": "string",
    "userEmail": "string",
    "userPhoneNumber": "string",
    "userDateOfBirth": "string (YYYY-MM-DD)",
    "addressClass": []
  }
  ```
- **Frontend Implementation:** [user.service.ts](../src/app/core/services/user.service.ts) - `createUser()` method

---

### 2. Get User by ID
- **Method:** `GET`
- **Endpoint:** `/user/{id}`
- **Path Variables:**
  - `id`: Long (user ID)
- **Response (200 OK):**
  ```json
  {
    "userId": 1,
    "userFirstName": "string",
    "userLastName": "string",
    "userEmail": "string",
    "userPhoneNumber": "string",
    "userDateOfBirth": "string (YYYY-MM-DD)",
    "addressClass": []
  }
  ```
- **Frontend Implementation:** [user.service.ts](../src/app/core/services/user.service.ts) - `getUserById(userId: number)` method

---

### 3. Get All Users
- **Method:** `GET`
- **Endpoint:** `/user/all`
- **Response (200 OK):**
  ```json
  [
    {
      "userId": 1,
      "userFirstName": "string",
      "userLastName": "string",
      "userEmail": "string",
      "userPhoneNumber": "string",
      "userDateOfBirth": "string (YYYY-MM-DD)",
      "addressClass": []
    }
  ]
  ```
- **Frontend Implementation:** [user.service.ts](../src/app/core/services/user.service.ts) - `getAllUsers()` method

---

### 4. Update User
- **Method:** `PUT`
- **Endpoint:** `/user/update/{id}`
- **Path Variables:**
  - `id`: Long (user ID)
- **Request Body:**
  ```json
  {
    "userFirstName": "string",
    "userLastName": "string",
    "userEmail": "string",
    "userPhoneNumber": "string",
    "userDateOfBirth": "string (YYYY-MM-DD)",
    "addressClass": []
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "userId": 1,
    "userFirstName": "string",
    "userLastName": "string",
    "userEmail": "string",
    "userPhoneNumber": "string",
    "userDateOfBirth": "string (YYYY-MM-DD)",
    "addressClass": []
  }
  ```
- **Frontend Implementation:** [user.service.ts](../src/app/core/services/user.service.ts) - `updateUser(userId: number, updateData: UpdateUserRequestDto)` method

---

### 5. Delete User
- **Method:** `DELETE`
- **Endpoint:** `/user/delete/{id}`
- **Path Variables:**
  - `id`: Long (user ID)
- **Response (204 No Content):** No response body
- **Frontend Implementation:** [user.service.ts](../src/app/core/services/user.service.ts) - `deleteUser(userId: number)` method

---

## Frontend Changes Summary

### 1. **New Services Created**

#### UserService (`src/app/core/services/user.service.ts`)
Handles all user CRUD operations:
- `createUser(userData: UserRequestDto): Observable<UserResponseDto>`
- `getUserById(userId: number): Observable<UserResponseDto>`
- `getAllUsers(): Observable<UserResponseDto[]>`
- `updateUser(userId: number, updateData: UpdateUserRequestDto): Observable<UpdateUserResponseDto>`
- `deleteUser(userId: number): Observable<void>`

#### Updated AuthService (`src/app/core/services/auth.service.ts`)
- `login(credentials: LoginRequest): Observable<AuthResponse>`
- `signup(signupData: any): Observable<AuthResponse>`
- `refreshToken(): Observable<AuthResponse>`
- Auto-refresh mechanism with 60-second token expiration
- Token stored in localStorage with keys: `access_token` and `refresh_token`

### 2. **New Models/Interfaces Created**

#### UserModels (`src/app/shared/models/user.models.ts`)
- `AddressClassRequestDto`
- `AddressClassResponseDto`
- `UserRequestDto`
- `UserResponseDto`
- `UpdateUserRequestDto`
- `UpdateUserResponseDto`

### 3. **Updated Components**

#### SignupComponent (`src/app/pages/signup/signup.component.ts` & `.html`)
**Added Form Fields:**
- First Name (text, required, min 2 chars)
- Last Name (text, required, min 2 chars)
- **Email (NEW - email, required)**
- **Phone Number (NEW - tel, required, min 10 digits)**
- **Date of Birth (NEW - date picker, required)**
- Username (text, required, min 3 chars)
- Password (password, required, min 6 chars)
- Confirm Password (password, required, must match)

**Updated Validation:**
- Added email validation (valid email format)
- Added phone number validation (10+ digits pattern)
- Added date of birth validation

**Submission Data Structure:**
```typescript
{
  userFirstName: string;
  userLastName: string;
  userEmail: string;
  userPhoneNumber: string;
  userDateOfBirth: string;
  userLoginId: string;
  userPassword: string;
}
```

### 4. **HTTP Configuration**

**API Base URLs:**
- Auth API: `http://localhost:8001/api/auth`
- User API: `http://localhost:8001/user`
- Database: MySQL on `localhost:3306` (database: `StartAhb_TASK_1`)

**HTTP Interceptor:** [auth.interceptor.ts](../src/app/core/interceptors/auth.interceptor.ts)
- Automatically attaches JWT token to all HTTP requests
- Handles 401 Unauthorized responses
- Implements token refresh logic
- Supports concurrent request handling during refresh

---

## Key Features Implemented

1. **JWT Token Management**
   - Access token and refresh token storage
   - Automatic token refresh 10 seconds before expiration
   - Secure token handling via local storage

2. **Authentication Flow**
   - Login with username/password
   - Signup with complete user information
   - Token-based authorization
   - Automatic logout on token expiration

3. **User Management**
   - Create new users
   - Retrieve user information
   - Update user details
   - Delete users
   - List all users

4. **Form Validation**
   - Client-side validation for all fields
   - Email format validation
   - Phone number format validation
   - Password matching validation
   - Real-time error messages

5. **Security**
   - CORS enabled on authentication endpoints
   - Password encryption on backend
   - JWT-based authentication
   - Automatic token refresh mechanism
   - Authorization header handling

---

## Testing the API Endpoints

### Using cURL Examples:

**Login:**
```bash
curl -X POST http://localhost:8001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"userLoginId":"testuser","password":"password123"}'
```

**Signup:**
```bash
curl -X POST http://localhost:8001/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "userFirstName":"John",
    "userLastName":"Doe",
    "userEmail":"john@example.com",
    "userPhoneNumber":"1234567890",
    "userDateOfBirth":"1990-01-15",
    "userLoginId":"johndoe",
    "userPassword":"password123"
  }'
```

**Get User:**
```bash
curl -X GET http://localhost:8001/user/1 \
  -H "Authorization: Bearer <token>"
```

**Get All Users:**
```bash
curl -X GET http://localhost:8001/user/all \
  -H "Authorization: Bearer <token>"
```

---

## Notes for Developers

1. **Date Format:** All dates should use ISO 8601 format (YYYY-MM-DD)
2. **Phone Number:** Must be at least 10 digits
3. **Password:** Backend passwords are encrypted using BCrypt
4. **Token Expiration:** Access tokens expire in 60 seconds, refresh tokens in 7 days
5. **CORS:** Authentication endpoints allow cross-origin requests from any origin
6. **AddressClass:** Currently not fully implemented in frontend forms but supported by backend DTOs
