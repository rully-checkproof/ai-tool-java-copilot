# API Examples for getAllTasks Endpoint

## Endpoint Details
- **URL**: `GET /api/tasks`
- **Description**: Get all tasks with pagination and optional filtering
- **Parameters**:
  - `page` (optional): Page number (default: 0)
  - `size` (optional): Page size (default: 20)
  - `sort` (optional): Sort criteria (e.g., "title,asc" or "startDate,desc")
  - `status` (optional): Filter by task status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
  - `startDate` (optional): Filter tasks starting after this date (ISO 8601 format)
  - `endDate` (optional): Filter tasks ending before this date (ISO 8601 format)

## Example Requests

### 1. Basic Request - Get All Tasks (Default Pagination)
```http
GET /api/tasks
Host: localhost:8080
Content-Type: application/json
```

**Response**: Returns first 20 tasks with default pagination.

---

### 2. Pagination Examples

#### Get Second Page with 10 Items
```http
GET /api/tasks?page=1&size=10
Host: localhost:8080
Content-Type: application/json
```

#### Get Tasks with Custom Page Size
```http
GET /api/tasks?page=0&size=5
Host: localhost:8080
Content-Type: application/json
```

---

### 3. Sorting Examples

#### Sort by Title (Ascending)
```http
GET /api/tasks?sort=title,asc
Host: localhost:8080
Content-Type: application/json
```

#### Sort by Start Date (Descending)
```http
GET /api/tasks?sort=startDate,desc
Host: localhost:8080
Content-Type: application/json
```

#### Multiple Sort Criteria
```http
GET /api/tasks?sort=priority,desc&sort=startDate,asc
Host: localhost:8080
Content-Type: application/json
```

---

### 4. Status Filtering Examples

#### Get Only Pending Tasks
```http
GET /api/tasks?status=PENDING
Host: localhost:8080
Content-Type: application/json
```

#### Get In Progress Tasks with Pagination
```http
GET /api/tasks?status=IN_PROGRESS&page=0&size=15
Host: localhost:8080
Content-Type: application/json
```

#### Get Completed Tasks
```http
GET /api/tasks?status=COMPLETED
Host: localhost:8080
Content-Type: application/json
```

#### Get Cancelled Tasks
```http
GET /api/tasks?status=CANCELLED
Host: localhost:8080
Content-Type: application/json
```

---

### 5. Date Range Filtering Examples

#### Get Tasks Starting After a Specific Date
```http
GET /api/tasks?startDate=2025-07-01T00:00:00
Host: localhost:8080
Content-Type: application/json
```

#### Get Tasks Ending Before a Specific Date
```http
GET /api/tasks?endDate=2025-07-31T23:59:59
Host: localhost:8080
Content-Type: application/json
```

#### Get Tasks Within a Date Range
```http
GET /api/tasks?startDate=2025-07-01T00:00:00&endDate=2025-07-31T23:59:59
Host: localhost:8080
Content-Type: application/json
```

#### Get Today's Tasks
```http
GET /api/tasks?startDate=2025-07-07T00:00:00&endDate=2025-07-07T23:59:59
Host: localhost:8080
Content-Type: application/json
```

#### Get This Week's Tasks
```http
GET /api/tasks?startDate=2025-07-07T00:00:00&endDate=2025-07-13T23:59:59
Host: localhost:8080
Content-Type: application/json
```

---

### 6. Combined Filtering Examples

#### Pending Tasks for This Week with Pagination
```http
GET /api/tasks?status=PENDING&startDate=2025-07-07T00:00:00&endDate=2025-07-13T23:59:59&page=0&size=10
Host: localhost:8080
Content-Type: application/json
```

#### Completed Tasks from Last Month, Sorted by End Date
```http
GET /api/tasks?status=COMPLETED&startDate=2025-06-01T00:00:00&endDate=2025-06-30T23:59:59&sort=endDate,desc
Host: localhost:8080
Content-Type: application/json
```

#### In Progress Tasks with Custom Pagination and Sorting
```http
GET /api/tasks?status=IN_PROGRESS&page=1&size=5&sort=priority,desc&sort=startDate,asc
Host: localhost:8080
Content-Type: application/json
```

---

### 7. cURL Examples

#### Basic Request
```bash
curl -X GET "http://localhost:8080/api/tasks" \
  -H "Content-Type: application/json"
```

#### With Pagination and Status Filter
```bash
curl -X GET "http://localhost:8080/api/tasks?page=0&size=10&status=PENDING" \
  -H "Content-Type: application/json"
```

#### With Date Range and Sorting
```bash
curl -X GET "http://localhost:8080/api/tasks?startDate=2025-07-01T00:00:00&endDate=2025-07-31T23:59:59&sort=startDate,desc" \
  -H "Content-Type: application/json"
```

#### Complex Filter with All Parameters
```bash
curl -X GET "http://localhost:8080/api/tasks?status=IN_PROGRESS&startDate=2025-07-07T00:00:00&endDate=2025-07-14T23:59:59&page=0&size=20&sort=priority,desc" \
  -H "Content-Type: application/json"
```

---

### 8. JavaScript/Fetch Examples

#### Basic Request
```javascript
fetch('/api/tasks')
  .then(response => response.json())
  .then(data => console.log(data));
```

#### With Query Parameters
```javascript
const params = new URLSearchParams({
  page: 0,
  size: 10,
  status: 'PENDING',
  startDate: '2025-07-07T00:00:00',
  endDate: '2025-07-14T23:59:59',
  sort: 'startDate,desc'
});

fetch(`/api/tasks?${params}`)
  .then(response => response.json())
  .then(data => console.log(data));
```

#### Async/Await Example
```javascript
async function getTasks(filters = {}) {
  try {
    const params = new URLSearchParams(filters);
    const response = await fetch(`/api/tasks?${params}`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching tasks:', error);
  }
}

// Usage examples
getTasks({ status: 'PENDING', page: 0, size: 10 });
getTasks({ startDate: '2025-07-07T00:00:00', sort: 'priority,desc' });
```

---

### 9. Expected Response Format

```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "title": "Complete project documentation",
      "description": "Write comprehensive documentation for the project",
      "startDate": "2025-07-07T09:00:00",
      "endDate": "2025-07-07T17:00:00",
      "priority": "HIGH",
      "status": "PENDING",
      "recurrenceType": "NONE",
      "recurrencePattern": null,
      "participants": [
        {
          "id": "456e7890-e89b-12d3-a456-426614174000",
          "name": "John Doe",
          "title": "Developer",
          "department": "Engineering",
          "role": "USER"
        }
      ]
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageSize": 20,
    "pageNumber": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### 10. Testing with Postman

#### Collection Variables
- `baseUrl`: `http://localhost:8080`
- `apiPath`: `/api/tasks`

#### Example Postman Requests

1. **Get All Tasks**
   - Method: GET
   - URL: `{{baseUrl}}{{apiPath}}`

2. **Get Pending Tasks**
   - Method: GET
   - URL: `{{baseUrl}}{{apiPath}}`
   - Params: `status=PENDING`

3. **Get Tasks with Date Range**
   - Method: GET
   - URL: `{{baseUrl}}{{apiPath}}`
   - Params: 
     - `startDate=2025-07-07T00:00:00`
     - `endDate=2025-07-14T23:59:59`

---

### Notes

1. **Date Format**: All dates should be in ISO 8601 format (`YYYY-MM-DDTHH:mm:ss`)
2. **Status Values**: Valid status values are `PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
3. **Pagination**: Default page size is typically 20, page numbers start from 0
4. **Sorting**: Use format `property,direction` where direction is `asc` or `desc`
5. **URL Encoding**: Remember to URL encode special characters in query parameters when needed

These examples cover all the major use cases for the `getAllTasks` endpoint with various combinations of pagination, filtering, and sorting options.
