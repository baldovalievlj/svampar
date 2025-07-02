INSERT INTO "user"."user" (
    username,
    password,
    email,
    first_name,
    last_name,
    phone_number,
    role,
    deleted
) VALUES (
             'superadmin',
             '$2a$10$eQUzKbf2ybcj2gbRZru7bOPCDx8Ty82nNcH4RMIYUb9Z3YgjgJS4e',
             'test@example.com',
             'Test',
             'User',
             '1234567890',
             1,
             FALSE
         );