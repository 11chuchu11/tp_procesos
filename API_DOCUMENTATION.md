# 📚 API Documentation - Scrim Management System

**Base URL:** `http://localhost:8080`

---

## 🔐 Authentication Endpoints

### 1. Register User

**Endpoint:** `POST /api/auth/register`

**Description:** Register a new user in the system.

**Headers:**

```
Content-Type: application/json
```

**Request Body:**

```json
{
  "username": "string", // Required, 3-100 characters, alphanumeric + underscore
  "email": "string", // Required, valid email format, max 255 characters
  "password": "string" // Required
}
```

**Response (201 Created):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com"
}
```

**Error Response (400 Bad Request):**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists"
}
```

**Validation Rules:**

- `username`: 3-100 chars, only letters, numbers, underscore
- `email`: Valid email format
- `password`: Required

---

### 2. Login

**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and receive JWT token.

**Headers:**

```
Content-Type: application/json
```

**Request Body:**

```json
{
  "email": "string", // Required
  "password": "string" // Required
}
```

**Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com"
}
```

**Error Response (401 Unauthorized):**

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

---

## 👤 Profile Endpoints

> **Note:** All profile endpoints require authentication.  
> Include the JWT token in the Authorization header: `Authorization: Bearer {token}`

---

### 3. Create Profile

**Endpoint:** `POST /api/profile`

**Description:** Create a profile for the authenticated user. Each user can only have one profile.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body:**

```json
{
  "mainGameId": 1,      // Required, Long (game ID)
  "tierId": 4,          // Required, Long (tier ID)
  "region": "string"    // Required
}
```

**Example:**

```json
{
  "mainGame": "Valorant",
  "tier": "Gold",
  "region": "LATAM-SUR"
}
```

**Response (201 Created):**

```json
{
  "profileId": 1,
  "userId": 5,
  "username": "john_doe",
  "mainGame": "Valorant",
  "tier": "Gold",
  "region": "LATAM-SUR",
  "status": "AVAILABLE"
}
```

**Error Responses:**

```json
// 400 - Profile already exists
{
  "status": 400,
  "error": "Bad Request",
  "message": "Profile already exists for this user"
}

// 401 - Not authenticated
{
  "status": 401,
  "error": "Unauthorized",
  "message": "User not authenticated"
}
```

**Validation Rules:**

- `mainGameId`: Required, must be a valid game ID
- `tierId`: Required, must be a valid tier ID for the selected game
- `region`: Required, not blank

**Important:** The system validates that the tier belongs to the selected game.

---

### 4. Update Profile

**Endpoint:** `PUT /api/profile`

**Description:** Update the authenticated user's profile. All fields are optional - only provided fields will be updated.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body:**

```json
{
  "mainGame": "string", // Optional
  "tier": "string", // Optional
  "region": "string" // Optional
}
```

**Example - Update only tier:**

```json
{
  "tier": "Platinum"
}
```

**Example - Update all fields:**

```json
{
  "mainGame": "League of Legends",
  "tier": "Diamond",
  "region": "NA"
}
```

**Response (200 OK):**

```json
{
  "profileId": 1,
  "userId": 5,
  "username": "john_doe",
  "mainGame": "League of Legends",
  "tier": "Diamond",
  "region": "NA",
  "status": "AVAILABLE"
}
```

**Error Responses:**

```json
// 400 - Profile not found
{
  "status": 400,
  "error": "Bad Request",
  "message": "Profile not found for user: john_doe"
}

// 400 - Cannot update while in scrim
{
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot update profile while in a scrim. Status: BUSY"
}

// 401 - Not authenticated
{
  "status": 401,
  "error": "Unauthorized",
  "message": "User not authenticated"
}
```

**Behavior:**

- Only updates fields that are provided (not null)
- Cannot update profile while status is `BUSY` (in a scrim)
- Profile must exist before updating

---

### 5. Get My Profile

**Endpoint:** `GET /api/profile`

**Description:** Get the authenticated user's profile information.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body:** None

**Response (200 OK):**

```json
{
  "profileId": 1,
  "userId": 5,
  "username": "john_doe",
  "mainGame": "Valorant",
  "tier": "Gold",
  "region": "LATAM-SUR",
  "status": "AVAILABLE"
}
```

**Error Response (404 Not Found):**

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Profile not found for user: john_doe"
}
```

---

## 🎮 Scrim Endpoints

> **Note:** All scrim endpoints (except GET /api/scrim) require authentication.  
> Include the JWT token in the Authorization header: `Authorization: Bearer {token}`

---

### 6. Search Scrims

**Endpoint:** `GET /api/scrim`

**Description:** Search scrims with optional filters. Returns all scrims if no filters provided.

**Headers:**

```
Content-Type: application/json
```

**Query Parameters:** (All optional)

```
?gameId=1
&formatType=5v5
&region=LATAM-SUR
&minTier=Gold
&maxTier=Platinum
&status=SEARCHING
```

**Parameters:**

- `gameId` (Long): Filter by game ID
- `formatType` (String): Filter by format (`1v1`, `3v3`, `5v5`)
- `region` (String): Filter by region
- `minTier` (String): Filter by minimum tier
- `maxTier` (String): Filter by maximum tier
- `status` (String): Filter by status (`SEARCHING`, `LOBBYREADY`, `CONFIRMED`, `INGAME`, `FINISHED`, `CANCELLED`)

**Response (200 OK):**

```json
[
  {
    "scrimId": 1,
    "status": "SEARCHING",
    "formatType": "5v5",
    "lobbyFull": false,
    "gameId": 1,
    "gameName": "Valorant",
    "minTierId": 4,
    "minTierName": "Gold",
    "maxTierId": 5,
    "maxTierName": "Platinum",
    "region": "LATAM-SUR",
    "scheduledTime": "2025-11-09T19:00:00",
    "totalPlayers": null,
    "confirmedPlayers": null
  }
]
```

**Example Requests:**

```bash
# Get all scrims
GET /api/scrim

# Get scrims for Valorant
GET /api/scrim?gameId=1

# Get available 5v5 scrims in LATAM-SUR
GET /api/scrim?formatType=5v5&region=LATAM-SUR&status=SEARCHING

# Get Gold-Platinum scrims (using tier IDs)
GET /api/scrim?minTierId=4&maxTierId=5
```

---

### 7. Create Scrim

**Endpoint:** `POST /api/scrim`

**Description:** Create a new scrim. The creator is automatically added to the lobby and the system attempts to auto-fill with available players. The game for the scrim is automatically taken from the creator's profile main game.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body:**

```json
{
  "formatType": "string",          // Required, must be "1v1", "3v3", or "5v5"
  "minTierId": 4,                  // Optional, Long, minimum tier ID requirement
  "maxTierId": 5,                  // Optional, Long, maximum tier ID requirement
  "regionId": 1,                   // Optional, Long, region ID
  "scheduledTime": "2025-11-09T19:00:00"  // Optional, ISO 8601 DateTime (default: now + 1 hour)
}
```

**Example - Basic:**

```json
{
  "formatType": "5v5"
}
```

**Example - Complete:**

```json
{
  "formatType": "5v5",
  "minTierId": 4,
  "maxTierId": 5,
  "regionId": 1,
  "scheduledTime": "2025-11-09T20:00:00"
}
```

**Response (201 Created):**

```json
{
  "scrimId": 5,
  "status": "SEARCHING",
  "formatType": "5v5",
  "lobbyFull": false,
  "gameId": 1,
  "gameName": "Valorant",
  "minTierId": 4,
  "minTierName": "Gold",
  "maxTierId": 5,
  "maxTierName": "Platinum",
  "region": "LATAM-SUR",
  "scheduledTime": "2025-11-09T20:00:00",
  "totalPlayers": null,
  "confirmedPlayers": null
}
```

**Error Response (400 Bad Request):**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Your tier (Silver) is not within the required range for this scrim"
}
```

**Validation Rules:**

- `formatType`: Must be `1v1`, `3v3`, or `5v5`
- `gameId`: Must be a valid game ID
- `minTierId` and `maxTierId`: Must be valid tier IDs that belong to the selected game
- `minTier` cannot be higher than `maxTier` (validated by rank)
- Creator's tier must be within `minTier` and `maxTier` range

**Behavior:**

- If `scheduledTime` is null → defaults to `now + 1 hour`
- Creator is automatically added to the lobby
- System auto-fills lobby with available and eligible players
- If lobby becomes full → status changes to `LOBBYREADY` automatically

---

### 8. Get Scrim by ID

**Endpoint:** `GET /api/scrim/{id}`

**Description:** Get details of a specific scrim.

**Headers:**

```
Content-Type: application/json
```

**Path Parameters:**

- `id` (Long): Scrim ID

**Response (200 OK):**

```json
{
  "scrimId": 1,
  "status": "LOBBYREADY",
  "formatType": "5v5",
  "lobbyFull": true,
  "gameId": 1,
  "gameName": "Valorant",
  "minTier": "Gold",
  "maxTier": "Platinum",
  "region": "LATAM-SUR",
  "scheduledTime": "2025-11-09T19:00:00",
  "totalPlayers": 10,
  "confirmedPlayers": 3
}
```

**Error Response (404 Not Found):**

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Scrim not found with id: 999"
}
```

---

### 9. Apply to Scrim

**Endpoint:** `POST /api/scrim/{id}/apply`

**Description:** Apply to join an existing scrim. Player must meet tier requirements.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters:**

- `id` (Long): Scrim ID

**Request Body:** None

**Response (200 OK):**

```json
{
  "scrimId": 1,
  "status": "LOBBYREADY",
  "formatType": "5v5",
  "lobbyFull": true,
  "gameId": 1,
  "gameName": "Valorant",
  "minTier": "Gold",
  "maxTier": "Platinum",
  "region": "LATAM-SUR",
  "scheduledTime": "2025-11-09T19:00:00",
  "totalPlayers": 10,
  "confirmedPlayers": 0
}
```

**Error Responses:**

```json
// 400 - Already in scrim
{
  "status": 400,
  "error": "Bad Request",
  "message": "You are already in this scrim"
}

// 400 - Tier not eligible
{
  "status": 400,
  "error": "Bad Request",
  "message": "Your tier (Bronze) is not within the required range for this scrim"
}

// 400 - Lobby full
{
  "status": 400,
  "error": "Bad Request",
  "message": "Failed to add player to the lobby. Lobby may be full"
}
```

**Behavior:**

- Player's tier must be within `minTier` and `maxTier` range
- Player is added to a random team (automatic balancing)
- Player's profile status changes to `BUSY`
- If lobby becomes full → status changes to `LOBBYREADY` and confirmation records are created

---

### 10. Confirm Participation

**Endpoint:** `POST /api/scrim/{id}/confirm`

**Description:** Confirm participation in a scrim that is in LOBBYREADY state.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters:**

- `id` (Long): Scrim ID

**Request Body:** None

**Response (200 OK):**

```json
{
  "scrimId": 1,
  "status": "CONFIRMED", // or "INGAME" if scheduled time reached
  "formatType": "5v5",
  "lobbyFull": true,
  "gameId": 1,
  "gameName": "Valorant",
  "minTier": "Gold",
  "maxTier": "Platinum",
  "region": "LATAM-SUR",
  "scheduledTime": "2025-11-09T19:00:00",
  "totalPlayers": 10,
  "confirmedPlayers": 10
}
```

**Error Responses:**

```json
// 400 - Wrong state
{
  "status": 400,
  "error": "Bad Request",
  "message": "Scrim is not ready for confirmations. Current status: SEARCHING"
}

// 400 - Not in scrim
{
  "status": 400,
  "error": "Bad Request",
  "message": "You are not in this scrim"
}

// 400 - Already confirmed
{
  "status": 400,
  "error": "Bad Request",
  "message": "You have already confirmed"
}
```

**Behavior:**

- Only works for scrims in `LOBBYREADY` state
- If all players confirm:
  - **If `scheduledTime` > now:** → status becomes `CONFIRMED` (waiting)
  - **If `scheduledTime` <= now:** → status becomes `INGAME` (start immediately)

---

### 11. Get Confirmations

**Endpoint:** `GET /api/scrim/{id}/confirmations`

**Description:** Get confirmation status of all players in a scrim.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters:**

- `id` (Long): Scrim ID

**Response (200 OK):**

```json
[
  {
    "profileId": 1,
    "username": "player1",
    "confirmed": true,
    "confirmedAt": "2025-11-09T18:30:00"
  },
  {
    "profileId": 2,
    "username": "player2",
    "confirmed": false,
    "confirmedAt": null
  },
  {
    "profileId": 3,
    "username": "player3",
    "confirmed": true,
    "confirmedAt": "2025-11-09T18:32:00"
  }
]
```

**Error Response (404 Not Found):**

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Scrim not found with id: 999"
}
```

---

### 12. Cancel Scrim

**Endpoint:** `POST /api/scrim/{id}/cancel`

**Description:** Cancel a scrim. Only the creator can cancel.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters:**

- `id` (Long): Scrim ID

**Request Body:** None

**Response (200 OK):**

```json
{
  "scrimId": 1,
  "status": "CANCELLED",
  "formatType": "5v5",
  "lobbyFull": true,
  "gameId": 1,
  "gameName": "Valorant",
  "minTier": "Gold",
  "maxTier": "Platinum",
  "region": "LATAM-SUR",
  "scheduledTime": "2025-11-09T19:00:00",
  "totalPlayers": null,
  "confirmedPlayers": null
}
```

**Error Responses:**

```json
// 400 - Not creator
{
  "status": 400,
  "error": "Bad Request",
  "message": "Only the creator can cancel the scrim"
}

// 400 - Invalid state
{
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot cancel a scrim in INGAME state"
}
```

**Behavior:**

- Only the creator can cancel
- All players' profile status changes back to `AVAILABLE`
- All confirmation records are deleted

---

### 13. Finish Scrim

**Endpoint:** `POST /api/scrim/{id}/finish`

**Description:** Mark a scrim as finished. Only works for scrims in INGAME state.

**Headers:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters:**

- `id` (Long): Scrim ID

**Request Body:** None

**Response (200 OK):**

```json
{
  "scrimId": 1,
  "status": "FINISHED",
  "formatType": "5v5",
  "lobbyFull": true,
  "gameId": 1,
  "gameName": "Valorant",
  "minTier": "Gold",
  "maxTier": "Platinum",
  "region": "LATAM-SUR",
  "scheduledTime": "2025-11-09T19:00:00",
  "totalPlayers": null,
  "confirmedPlayers": null
}
```

**Error Response (400 Bad Request):**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot finish a scrim in SEARCHING state"
}
```

**Behavior:**

- Only works for scrims in `INGAME` state
- All players' profile status changes back to `AVAILABLE`

---

## 📊 Status Flow Diagram

```
SEARCHING → (lobby full) → LOBBYREADY → (all confirm) → CONFIRMED/INGAME → (finish) → FINISHED
    ↓                           ↓                            ↓
(cancel)                    (cancel)                     (cancel)
    ↓                           ↓                            ↓
CANCELLED                   CANCELLED                    CANCELLED
```

**Status Transitions:**

- `SEARCHING`: Initial state, accepting applications
- `LOBBYREADY`: Lobby is full, waiting for confirmations
- `CONFIRMED`: All players confirmed, waiting for scheduled time
- `INGAME`: Game in progress (either scheduled time reached or all confirmed after scheduled time)
- `FINISHED`: Game completed
- `CANCELLED`: Scrim cancelled by creator

---

## 🔑 Common Error Responses

### 401 Unauthorized

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "User not authenticated"
}
```

### 400 Validation Error

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input data",
  "details": ["Format type must be 1v1, 3v3, or 5v5", "Game ID is required"]
}
```

### 404 Not Found

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Scrim not found with id: 999"
}
```

### 500 Internal Server Error

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

---

## 📝 Usage Examples

### Complete Flow Example

```bash
# 1. Register
POST /api/auth/register
{
  "username": "john_gamer",
  "email": "john@example.com",
  "password": "SecurePass123"
}
# Response: { "token": "eyJhbGci..." }

# 2. Create Profile
POST /api/profile
Authorization: Bearer eyJhbGci...
{
  "mainGameId": 1,
  "tierId": 4,
  "region": "LATAM-SUR"
}
# Response: { "profileId": 1, "mainGameName": "Valorant", "tierName": "Gold", "status": "AVAILABLE" }

# 3. Create Scrim
POST /api/scrim
Authorization: Bearer eyJhbGci...
{
  "formatType": "5v5",
  "gameId": 1,
  "minTierId": 4,
  "maxTierId": 5,
  "region": "LATAM-SUR"
}
# Response: { "scrimId": 1, "status": "SEARCHING" }

# 4. Another player applies
POST /api/scrim/1/apply
Authorization: Bearer {other_player_token}
# Response: { "status": "SEARCHING" or "LOBBYREADY" if full }

# 5. Check confirmations (if LOBBYREADY)
GET /api/scrim/1/confirmations
Authorization: Bearer eyJhbGci...
# Response: [{ "username": "john_gamer", "confirmed": false }, ...]

# 6. Confirm participation
POST /api/scrim/1/confirm
Authorization: Bearer eyJhbGci...
# Response: { "status": "CONFIRMED" or "INGAME" }

# 7. Finish game
POST /api/scrim/1/finish
Authorization: Bearer eyJhbGci...
# Response: { "status": "FINISHED" }
```

---

## 🎯 Notes

1. **Authentication:** All profile and scrim endpoints (except GET /api/scrim) require a valid JWT token
2. **Profile Required:** You must create a profile before creating or joining scrims
3. **Profile Status:** Players are automatically marked as `BUSY` when joining a scrim and `AVAILABLE` when it finishes/is cancelled
4. **Profile Updates:** Cannot update profile while status is `BUSY` (in a scrim)
5. **Auto-fill:** When creating a scrim, the system automatically searches for available players matching the criteria
6. **Scheduled Time:** If not provided, defaults to current time + 1 hour
7. **Confirmation Logic:**
   - If all players confirm before scheduled time → `CONFIRMED` (waiting)
   - If all players confirm after scheduled time → `INGAME` (start immediately)
8. **Team Balancing:** Players are automatically distributed evenly across two teams

---

**Last Updated:** November 9, 2025  
**API Version:** 1.0
