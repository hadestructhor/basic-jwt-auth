# About

This project is a remake of the project found [here](https://www.bezkoder.com/spring-boot-jwt-authentication/) entirely made in Kotlin to test user authentication and authorization via [jwt](https://fr.wikipedia.org/wiki/JSON_Web_Token) tokens.

# Data used in local DB

The database used in the application properties is named **basic-jwt-auth-db**.

You will need to change the name of the db to the one you'll create and change this attribute in the file **application.properties**:

````properties
spring.datasource.url=jdbc:postgresql://localhost:5432/basic-jwt-auth-db
````

For exemple, if you've named the DB you created **authdb** you'll replace that property with the following:

````properties
spring.datasource.url=jdbc:postgresql://localhost:5432/authdb
````



## Inserting initial data

You'll need to execute the following to add roles and users to your db.

### Run the application once

The password values were obtained by simply changing the main function in the **BasicJwtAuthApplication.kt** file to the following:

````kotlin
fun main(args: Array<String>) {
	val passwordEncoder = BCryptPasswordEncoder()
	val passwords = arrayOf("user", "mod", "admin")
	for(pass in passwords) println("$pass : ${passwordEncoder.encode(pass)}")
	runApplication<BasicJwtAuthApplication>(*args)
}
````

Once you run the application, the password shown will be used 

### Roles

To add new roles, simply insert them in the **roles** table that'll be generated once the application is run as follows: 

````sql
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MOD');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
````

### Users

To add new users, simply insert them in the **users** table that'll be generated once the application is run as follows:

````sql
insert into users VALUES (1, 'user@hadestructhor.com', '$2a$10$dAZYwwecdIItcseaTdBEJOeqIeoou1votr8FXMyLzNJ6hWK4P5Tfy', 'user');
insert into users VALUES (2, 'mod@hadestructhor.com', '$2a$10$KFex6vnQd7ETYbDIE6rQnOBEo9qGigB/eXJcNkSzfuD6bmgLwzUZ6', 'mod');
insert into users VALUES (3, 'admin@hadestructhor.com', '$2a$10$eJk2SjGhZL5z2CyhxaKbXeDgEDdWzPydEM6aPxG6bXBD.qg7SYxja', 'admin');
````

You can obviously change the passwords by replacing the 3 value in the inserts for users by the one that'll be printed out in the console when you'll run the application with the code above.

### Giving users roles

To give users roles, simply insert the user's id and the role's id into the **user_roles** table that'll be generated once the application is run as follows:

````sql
insert into user_roles values (1,1);
insert into user_roles values (2,1);
insert into user_roles values (2,2);
insert into user_roles values (3,1);
insert into user_roles values (3,2);
insert into user_roles values (3,3);
````

Here we are giving the user with the username **user** the role **ROLE_USER**.

The user **mod** the roles **ROLE_USER** and **ROLE_MOD**.

The user **admin** the roles **ROLE_USER**, **ROLE_MOD** and **ROLE_ADMIN**.

# Login a user

To login, you need to send a request to **/api/auth/signin** that matches this body:

````json
{
    "username": "username",
    "password": "password"
}
````

the username must exist in the database and the password must match the one stored once encoded using BCrypt.

# Sign in a user

To singin a new user that'll have the role **ROLE_USER**, send a request to **/api/auth/signup**:

````json
{
    "username": "username",
    "email": "email",
    "password": "password"
}
````

The email value can be nothing.

