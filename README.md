# AutoForex Backend CC


### Register URL /registrations
* Method POST | Request Body JSON

```json
{
    "firstName": "Test",
    "lastName": "Domain",
    "email": "test@gmail.com",
    "password": "letmein"
}
```
Respone Success

### Login URL /login
* Method GET
- [x] Email
- [x] Password

Response "login success" (if login is successful) "login fail" (else)
