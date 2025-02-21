-- Pour lancer ce script, modifier application.properties

-- Insertion des comptes
INSERT INTO Account (account_Number, solde) VALUES
    ('ACC123456', 5000.00),
    ('ACC789012', 3000.00),
    ('ACC345678', 10000.00),
    ('ACC901234', 7500.00);

-- Insertion des transferts
INSERT INTO Transfer (amount, transfer_Date, description, source_Account_Number, destination_Account_Number) VALUES
    (200.00, '2024-02-15', 'Paiement facture', 'ACC123456', 'ACC789012'),
    (500.00, '2024-02-16', 'Transfert personnel', 'ACC789012', 'ACC345678'),
    (1200.00, '2024-02-17', 'Paiement achat', 'ACC345678', 'ACC901234'),
    (300.00, '2024-02-18', 'Remboursement prêt', 'ACC901234', 'ACC123456');