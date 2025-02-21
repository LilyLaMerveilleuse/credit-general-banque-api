-- Pour lancer ce script, modifier application.properties

DROP TABLE IF EXISTS Transfer;
DROP TABLE IF EXISTS Account;

-- Création des comptes
CREATE TABLE Account (
                         account_Number VARCHAR(50) PRIMARY KEY,
                         solde DOUBLE
);

-- Création des transfers
CREATE TABLE Transfer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          amount DOUBLE NOT NULL,
                          transfer_Date DATE NOT NULL,
                          description VARCHAR(255),
                          source_Account_Number VARCHAR(50) NOT NULL,
                          destination_Account_Number VARCHAR(50) NOT NULL,
                          FOREIGN KEY (source_Account_Number) REFERENCES Account(account_Number),
                          FOREIGN KEY (destination_Account_Number) REFERENCES Account(account_Number)
);