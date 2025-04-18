-- Pour lancer ce script, modifier application.properties

DROP TABLE IF EXISTS Transfer;
DROP TABLE IF EXISTS Customer_BeneficiaryAccounts;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS UserCGB;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS TransferStatus;
DROP TABLE IF EXISTS RoleCGB;

-- Table des états de transfert
CREATE TABLE TransferStatus
(
    id   UUID default random_uuid() PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Table des roleCGBS
CREATE TABLE RoleCGB
(
    id   UUID default random_uuid() PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Table des utilisateurs
CREATE TABLE Customer
(
    id      UUID default random_uuid() PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    lei     VARCHAR(50)
);

-- Table des utilisateurs d'un client
CREATE TABLE UserCGB
(
    id          UUID default random_uuid() PRIMARY KEY,
    username    VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    customer_id UUID         NOT NULL,
    rolecgb_id  UUID         NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer (id),
    FOREIGN KEY (rolecgb_id)  REFERENCES RoleCGB  (id)
);

-- Table des comptes
CREATE TABLE Account
(
    iban     VARCHAR(34) PRIMARY KEY,
    solde DOUBLE NOT NULL,
    owner_id UUID,
    FOREIGN KEY (owner_id) REFERENCES Customer (id)
);

-- Table des comptes bénéficiaires autorisés (ManyToMany entre Customer et Account)
CREATE TABLE Customer_BeneficiaryAccounts
(
    customer_id  UUID        NOT NULL,
    account_iban VARCHAR(34) NOT NULL,
    PRIMARY KEY (customer_id, account_iban),
    FOREIGN KEY (customer_id) REFERENCES Customer (id),
    FOREIGN KEY (account_iban) REFERENCES Account (iban)
);

-- Table des transferts
CREATE TABLE Transfer
(
    id               UUID default random_uuid() PRIMARY KEY,
    amount DOUBLE NOT NULL,
    transfer_date    TIMESTAMP   NOT NULL,
    description      VARCHAR(255),
    status_id        UUID,
    lot_id           UUID,
    source_iban      VARCHAR(34) NOT NULL,
    destination_iban VARCHAR(34) NOT NULL,
    FOREIGN KEY (status_id) REFERENCES TransferStatus (id),
    FOREIGN KEY (source_iban) REFERENCES Account (iban),
    FOREIGN KEY (destination_iban) REFERENCES Account (iban)
);
