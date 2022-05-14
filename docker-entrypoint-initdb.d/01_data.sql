INSERT INTO airplanes (name, maxSpeed, weight, year, photo, price)
VALUES ('Cessna-150', 259, 730, 1956, 'C-150.png', 5000000),
       ('Cessna-172N', 228, 1159, 1956, 'С-172.png', 10000000),
       ('Cessna-182RG', 278, 1406, 1956, 'С-182.png', 15000000);

INSERT INTO users (login, password, role)
VALUES ('sasha', '$2a$10$SNH8NBLlR0JdPKzlVFF1Oe8fBMXJIUsztUXPYuzq1nHlmIj7oUSxy', 'USER');

INSERT INTO users (login, password, role)
VALUES ('pasha', '$2a$10$SNH8NBLlR0JdPKzlVFF1Oe8fBMXJIUsztUXPYuzq1nHlmIj7oUSxy', 'USER');

INSERT INTO users (login, password, role)
VALUES ('dasha', '$2a$10$SNH8NBLlR0JdPKzlVFF1Oe8fBMXJIUsztUXPYuzq1nHlmIj7oUSxy', 'USER');

INSERT INTO users (login, password, role)
VALUES ('admin', '$2a$10$SNH8NBLlR0JdPKzlVFF1Oe8fBMXJIUsztUXPYuzq1nHlmIj7oUSxy', 'ADMIN');

INSERT INTO users (login, password, role)
VALUES ('alex', '$2a$10$SNH8NBLlR0JdPKzlVFF1Oe8fBMXJIUsztUXPYuzq1nHlmIj7oUSxy', 'USER');

INSERT INTO applications (airplane_id, email)
VALUES (1, 'my@email')
