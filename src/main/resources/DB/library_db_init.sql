use master
go
CREATE DATABASE LibraryDB;
GO
USE LibraryDB;
GO

-- USERS
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    fullname NVARCHAR(100),
    email NVARCHAR(100),
    phone NVARCHAR(20),
    address NVARCHAR(255),
    birthday DATE,
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- ROLES
CREATE TABLE Roles (
    id INT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(50) UNIQUE NOT NULL,
    name NVARCHAR(100),
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- USER_ROLE
CREATE TABLE User_Role (
	id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES Users(id),
    role_id INT FOREIGN KEY REFERENCES Roles(id)
);

-- PERMISSIONS (CHỨC NĂNG)
CREATE TABLE Permissions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) UNIQUE NOT NULL,
    name NVARCHAR(100),
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- ROLE_PERMISSION
CREATE TABLE Role_Permission (
	id INT IDENTITY(1,1) PRIMARY KEY,
    role_id INT FOREIGN KEY REFERENCES Roles(id),
    permission_id INT FOREIGN KEY REFERENCES Permissions(id)
);

-- CATEGORY
CREATE TABLE Categories (
    id INT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(50) UNIQUE NOT NULL,
    name NVARCHAR(100),
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- BOOK
CREATE TABLE Books (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255),
    author NVARCHAR(255),
    publisher NVARCHAR(255),
	published_date DATE,
    page_count INT,
    quantity INT,
    print_type NVARCHAR(50),
    language NVARCHAR(50),
    description NVARCHAR(MAX),
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
	thumbnail_url VARCHAR(255),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- BOOK_CATEGORY
CREATE TABLE Book_Category (
	id INT IDENTITY(1,1) PRIMARY KEY,
    book_id INT FOREIGN KEY REFERENCES Books(id),
    category_id INT FOREIGN KEY REFERENCES Categories(id)
);

-- BORROWING
CREATE TABLE Borrowings (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES Users(id),
    book_id INT FOREIGN KEY REFERENCES Books(id),
    borrow_date DATE NOT NULL,
    return_date DATE,
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- POST
CREATE TABLE Posts (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255),
    content NVARCHAR(MAX),
    user_id INT FOREIGN KEY REFERENCES Users(id),
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);

-- COMMENT
CREATE TABLE Comments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    content NVARCHAR(MAX),
    post_id INT FOREIGN KEY REFERENCES Posts(id),
    user_id INT FOREIGN KEY REFERENCES Users(id),
    created_date DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_date DATETIME,
    updated_by NVARCHAR(50),
    is_active BIT DEFAULT 1,
    is_deleted BIT DEFAULT 0
);
