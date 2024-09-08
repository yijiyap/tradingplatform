CREATE USER IF NOT EXISTS 'myuser'@'%' IDENTIFIED BY 'root';

-- Granting all privileges on the database to the user
GRANT ALL PRIVILEGES ON tradingplatform.* TO 'myuser'@'%';

-- Flush privileges to ensure changes are applied
FLUSH PRIVILEGES;

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS tradingplatform;

-- Use the tradingplatform database
USE tradingplatform;