INSERT INTO roles (name, description) VALUES ('ADMIN', 'Administrator');
INSERT INTO roles (name, description) VALUES ('ADVISOR', 'Advisor');
INSERT INTO roles (name, description) VALUES ('CUSTOMER', 'Customer');

INSERT INTO users (name, lastname, birthdate, address, phone, email, basesalary, dni, password, id_rol) VALUES
('ADMIN', 'ADMIN', '2025-07-04 00:00:00', 'Calle Falsa 123, Ciudad', '3116202601', 'admin@admin.com', 7500000, '1111', '$2a$10$uTxPd7JYQx34Tomm0nAmHea5nBp.MsU0FDDi/IM9tu41yKktltONC', 1),
('ADVISOR', 'ADVISOR', '2025-07-04 00:00:00', 'Calle Falsa 123, Ciudad', '3116202601', 'advisor@advisor.com', 7500000, '2222', '$2a$10$IPvc/HMbCIv1P0.XIThtJuNmaSq1W14R03oQ2UpFjGvp9A.8ZE8IO', 2),
('CUSTOMER', 'CUSTOMER', '2025-07-04 00:00:00', 'Calle Falsa 123, Ciudad', '3116202601', 'customer@customer.com', 7500000, '3333', '$2a$10$Pgt6ZB0gVjMy1l2Iuh6TCuc2JlxAGo/RQNxtqc.VAkc.o5A09m296', 3);