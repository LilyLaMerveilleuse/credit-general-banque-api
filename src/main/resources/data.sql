-- Insertion des états de transfert
INSERT INTO TransferStatus (name) VALUES
                                       ('Transfer créé'),
                                       ('Erreur de transfer'),
                                       ('Tranfer complété'),
                                       ('Transfer annulé');

-- Insertion de 20 comptes
INSERT INTO Account (iban, owner_name, solde) VALUES
                                                  ('FR7630006000010000000000001', 'Alice Dupont', 5000.00),
                                                  ('FR7630006000010000000000002', 'Bob Martin', 3200.50),
                                                  ('FR7630006000010000000000003', 'Charlie Durand', 150.75),
                                                  ('FR7630006000010000000000004', 'Diane Lefèvre', 2870.00),
                                                  ('FR7630006000010000000000005', 'Émile Petit', 490.30),
                                                  ('FR7630006000010000000000006', 'Fanny Rousseau', 8800.00),
                                                  ('FR7630006000010000000000007', 'Gilles Morel', 200.00),
                                                  ('FR7630006000010000000000008', 'Hélène Caron', 6300.90),
                                                  ('FR7630006000010000000000009', 'Isabelle Lemoine', 712.00),
                                                  ('FR7630006000010000000000010', 'Jacques Garnier', 4030.25),
                                                  ('FR7630006000010000000000011', 'Kévin Mercier', 120.40),
                                                  ('FR7630006000010000000000012', 'Laura Renaud', 945.70),
                                                  ('FR7630006000010000000000013', 'Marc Fontaine', 3100.00),
                                                  ('FR7630006000010000000000014', 'Nadine Blanchard', 480.55),
                                                  ('FR7630006000010000000000015', 'Olivier Renault', 3700.00),
                                                  ('FR7630006000010000000000016', 'Patricia Marchand', 1100.20),
                                                  ('FR7630006000010000000000017', 'Quentin Lambert', 299.90),
                                                  ('FR7630006000010000000000018', 'Romain Leclerc', 1570.65),
                                                  ('FR7630006000010000000000019', 'Sophie Fabre', 2000.00),
                                                  ('FR7630006000010000000000020', 'Thomas Moulin', 6750.00);