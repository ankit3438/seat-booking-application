# API Gateway with JWT Validation

This document explains how the API Gateway validates JWT tokens and forwards requests to microservices while preserving user information.

---

## 📋 Overview

The API Gateway acts as a single entry point for all client requests. It:
1. ✅ Validates JWT tokens from client requests
2. ✅ Extracts user information (userId, username, role)
3. ✅ Stores user information in request headers
4. ✅ Forwards authenticated requests to appropriate microservices
5. ✅ Blocks unauthenticated requests to protected endpoints

---

## 🔄 Request Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT REQUEST                            │
│   GET /booking/list                                              │
│   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9... │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│            JWT AUTHENTICATION FILTER                             │
│  - Extract token from Authorization header                      │
│  - Validate token signature and expiration                      │
└────────────────────────┬────────────────────────────────────────┘
                         │
        ┌────────────────┴────────────────┐
        │                                 │
        ▼                                 ▼
   ✅ VALID TOKEN                   ❌ INVALID/EXPIRED
        │                                 │
        ▼                                 ▼
┌──────────────────────────┐    ┌────────────────────────┐
│ Extract User Info:      │    │ Return 401 Unauthorized│
│ - username              │    │ Request blocked        │
│ - userId                │    └────────────────────────┘
│ - role                  │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────────────────────────────────────┐
│ Set Request Headers for Downstream Services:             │
│ - X-User-Name: john                                      │
│ - X-User-Id: 123                                         │
│ - X-User-Role: USER                                      │
│ - Authorization: Bearer <token>                          │
└────────────────────────┬─────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│ GATEWAY CONTROLLER                                      │
│ Routes requests to appropriate microservice:             │
│ - /auth/** → Auth Service (8082)                        │
│ - /booking/** → Booking Service (8083)                  │
│ - /payment/** → Payment Service (8084)                  │
│ - /seat/** → Seat Service (8085)                        │
└────────────────────────┬─────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│ MICROSERVICE RECEIVES REQUEST                             │
│ Can access user info from headers:                       │
│ - request.getHeader("X-User-Name")                       │
│ - request.getHeader("X-User-Id")                         │
│ - request.getHeader("X-User-Role")                       │
└────────────────────────┬─────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│ MICROSERVICE PROCESSES REQUEST                            │
│ Returns response back to API Gateway                      │
└────────────────────────┬─────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│ API GATEWAY RETURNS RESPONSE TO CLIENT                    │
└──────────────────────────────────────────────────────────┘
```

---

## 🛠️ Components

### 1. **JwtUtil.java** (`security/`)
Handles JWT token operations:
- `validateToken()` - Validates JWT signature and expiration
- `getUsernameFromToken()` - Extracts username from token
- `getUserIdFromToken()` - Extracts userId from token
- `getRoleFromToken()` - Extracts role from token

### 2. **JwtAuthenticationFilter.java** (`filter/`)
Intercepts all incoming requests:
- Extracts JWT token from `Authorization: Bearer <token>` header
- Validates token using JwtUtil
- Stores user information in request attributes
- Sets Spring Security context for authentication

### 3. **SecurityConfig.java** (`config/`)
Configures Spring Security:
- Registers JWT authentication filter
- Defines public endpoints (no auth required)
- Defines protected endpoints (auth required)
- Sets stateless session management

### 4. **GatewayController.java** (`controller/`)
Routes requests to microservices:
- Accepts requests for different services
- Preserves JWT token in headers
- Adds user information headers
- Forwards to appropriate microservice URL

### 5. **RestTemplateConfig.java** (`config/`)
Creates RestTemplate bean for HTTP communication with microservices.

---

## ⚙️ Configuration

### application.properties

```properties
# API Gateway Port
server.port=8081

# JWT Configuration
jwt.secret=your_super_secret_key_that_must_be_at_least_256_bits_long_for_HS256
jwt.expiration=86400000  # 24 hours in milliseconds

# Microservice URLs
service.auth-url=http://localhost:8082
service.booking-url=http://localhost:8083
service.payment-url=http://localhost:8084
service.seat-url=http://localhost:8085
```

---

## 🔐 Authentication Flow

### Step 1: User Login (Get JWT Token)

**Request:**
```http
POST http://localhost:8081/auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huIiwiZXhwIjoxNzAzNDExNjAwLCJpYXQiOjE3MDM0MTEzMDB9.xyz...",
  "type": "Bearer",
  "message": "Login successful"
}
```

### Step 2: Access Protected Resource (With JWT Token)

**Request:**
```http
GET http://localhost:8081/booking/list
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huIiwiZXhwIjoxNzAzNDExNjAwLCJpYXQiOjE3MDM0MTEzMDB9.xyz...
```

**API Gateway processes:**
1. Extracts token: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huIiwiZXhwIjoxNzAzNDExNjAwLCJpYXQiOjE3MDM0MTEzMDB9.xyz...`
2. Validates token
3. Extracts: username=`john`, userId=`1`, role=`USER`
4. Forwards to Booking Service with headers:
   - `Authorization: Bearer <token>`
   - `X-User-Name: john`
   - `X-User-Id: 1`
   - `X-User-Role: USER`

**Booking Service Response:**
```json
[
  {
    "id": 1,
    "eventId": 1,
    "userId": 1,
    "status": "confirmed"
  }
]
```

### Step 3: Invalid Token (Blocked)

**Request:**
```http
GET http://localhost:8081/booking/list
Authorization: Bearer invalid_token
```

**Response:**
```
401 Unauthorized
```

---

## 🧪 Testing with cURL

### Test 1: Register User
```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'
```

### Test 2: Login (Get JWT Token)
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'
```

Save the token from response.

### Test 3: Access Protected Resource
```bash
curl -X GET http://localhost:8081/booking/list \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

### Test 4: Without Token (Should Fail)
```bash
curl -X GET http://localhost:8081/booking/list
```

---

## 📦 How Microservices Access User Information

Each microservice receives user information in request headers.

### Example: In Booking Service Controller

```java
@RestController
@RequestMapping("/booking")
public class BookingController {

    @GetMapping("/list")
    public ResponseEntity<?> getBookings(HttpServletRequest request) {
        // Get user information from headers
        String username = request.getHeader("X-User-Name");
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");
        String token = request.getHeader("Authorization");

        System.out.println("User: " + username);
        System.out.println("UserId: " + userId);
        System.out.println("Role: " + role);

        // Your business logic here
        return ResponseEntity.ok("Bookings for user: " + username);
    }
}
```

### Example: Using @RequestHeader Annotation

```java
@GetMapping("/list")
public ResponseEntity<?> getBookings(
    @RequestHeader("X-User-Name") String username,
    @RequestHeader("X-User-Id") String userId,
    @RequestHeader("X-User-Role") String role) {
    
    System.out.println("User: " + username);
    System.out.println("UserId: " + userId);
    System.out.println("Role: " + role);
    
    return ResponseEntity.ok("Bookings for user: " + username);
}
```

---

## 🔑 Key Features

✅ **JWT Validation** - Validates token signature and expiration
✅ **User Info Propagation** - Passes user details to microservices
✅ **Stateless** - No session management, fully JWT-based
✅ **Secure** - Uses Spring Security and BCrypt
✅ **Scalable** - Supports multiple microservices
✅ **Error Handling** - Returns appropriate HTTP status codes

---

## 📝 Public vs Protected Endpoints

### Public Endpoints (No Auth Required)
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /public/**` - Any public endpoint

### Protected Endpoints (Auth Required)
- `GET /booking/**` - All booking operations
- `POST /booking/**` - Create bookings
- `GET /payment/**` - All payment operations
- `GET /seat/**` - All seat operations

---

## 🐛 Troubleshooting

### Issue: 401 Unauthorized

**Reason:** JWT token is missing or invalid

**Solution:**
1. Check if `Authorization` header is present
2. Verify token format: `Bearer <token>`
3. Check if token has expired (24 hours default)
4. Get new token by logging in again

### Issue: 403 Forbidden

**Reason:** User doesn't have required role

**Solution:**
1. Check user's role in token
2. Verify role matches endpoint requirements

### Issue: 502 Bad Gateway

**Reason:** Microservice is unreachable

**Solution:**
1. Check if microservice is running
2. Verify service URL in `application.properties`
3. Check network connectivity

---

## 🚀 Production Checklist

- [ ] Change `jwt.secret` to a strong, random key (min 256 bits)
- [ ] Enable HTTPS/TLS on API Gateway
- [ ] Set appropriate JWT expiration time
- [ ] Implement token refresh mechanism
- [ ] Add request logging and monitoring
- [ ] Enable CORS if needed
- [ ] Implement rate limiting
- [ ] Add comprehensive error handling
- [ ] Enable audit logging for security events

---

## 📚 Related Documentation

- See `SPRING_SECURITY_JWT_FLOW.md` for detailed JWT flow
- See Auth Service README for JWT token generation logic
