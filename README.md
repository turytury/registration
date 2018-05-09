Registration
================
This is an application to provide RESTful APIs for register new member.

Each item consist of
* id
* email
* password
* based salary
* type

### Technology
- Java (support Java 8 and later)
- Maven
- SpringBoot
- Swagger
- Git

### Running Project
Clone project from repository
```
git@github.com:turytury/registration.git
```
Build project
```
mvn clean install
```
Run project
```bash
mvn spring-boot:run
```

### RESTful API
**Create new user**
```
POST: /api/v1/users
```

* **Params Required:**

    ```
    {
        "email": "String",
        "password": "String",
        "salary": "String"
    }
    ```

* **Success Response:**

    ```
    {
        "id": 1,
        "email": "register.test@gmail.com",
        "password": "12345678",
        "salary": "15,000",
        "type": "Silver"
    }
    ```
    User type will be classified follows these criteria:
    * Silver:   15,000 - 30,000 THB/month
    * Gold:     30,001 - 50,000 THB/month
    * Platinum: more than 50,000 THB/month

* **Error Response:**

    ```
    {
      "status": "400",
      "message": ”{Error message}”
    }
    ```


**View all users**
```
GET: /api/v1/users
```

* **Success Response:**

    ```
    [
        {
            "id": 1,
            "email": "test1@gmail.com",
            "password": "12345678",
            "salary": "15,000",
            "type": "Silver"
        },
        {
            "id": 2,
            "email": "test2@gmail.com",
            "password": "12345678",
            "salary": "80,000",
            "type": "Platinum"
        }
    ]
    ```

* **Error Response:**

    ```
    {
      "status": "400",
      "message": ”{Error message}”
    }
    ```
    

If you want to play around, I also have Swagger documentation for each REST endpoints.

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
