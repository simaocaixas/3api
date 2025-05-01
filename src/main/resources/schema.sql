DROP TABLE IF EXISTS tree_table;
DROP TABLE IF EXISTS user_table;

CREATE TABLE user_table (
                            id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            username VARCHAR(30),
                            email VARCHAR(50),
                            first_name VARCHAR(30),
                            last_name VARCHAR(30),
                            password VARCHAR(300),
                            created_at TIMESTAMP,
                            last_login TIMESTAMP,
                            roles TEXT
);


CREATE TABLE tree_table (
                            id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            specie_name VARCHAR(30),
                            tree_height DOUBLE,
                            tree_age INT,
                            leaf_type VARCHAR(30),
                            x INT,
                            y INT,
                            creation_date TIMESTAMP,
                            last_actualization TIMESTAMP,
                            owner_id INTEGER,
                            FOREIGN KEY (owner_id) REFERENCES user_table(id)
);
