GRANT CREATE ON SCHEMA public TO test_user;
CREATE DATABASE test;
GRANT ALL PRIVILEGES ON DATABASE test TO test_user;
CREATE SCHEMA IF NOT EXISTS dbo AUTHORIZATION test_user;
