-- Pour lancer ce script, modifier application.properties

DROP TABLE IF EXISTS Transfer;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS TransferStatus;

-- Table des états de transfert
CREATE TABLE TransferStatus (
                                 id UUID default random_uuid() PRIMARY KEY,
                                 name VARCHAR(50) NOT NULL UNIQUE
);

-- Tables des comptes
CREATE TABLE Account (
                         iban VARCHAR(34) PRIMARY KEY,
                         owner_name VARCHAR(100) NOT NULL,
                         solde DOUBLE NOT NULL
);

-- Table des transferts
CREATE TABLE Transfer (
                          id UUID default random_uuid() PRIMARY KEY,
                          amount DOUBLE NOT NULL,
                          transfer_date DATE NOT NULL,
                          description VARCHAR(255),
                          status_id UUID,
                          source_iban VARCHAR(34) NOT NULL,
                          destination_iban VARCHAR(34) NOT NULL,
                          FOREIGN KEY (status_id) REFERENCES TransferStatus(id),
                          FOREIGN KEY (source_iban) REFERENCES Account(iban),
                          FOREIGN KEY (destination_iban) REFERENCES Account(iban)
);
