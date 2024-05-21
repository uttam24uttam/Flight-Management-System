INSERT INTO passenger (passenger_id, passenger_name, passenger_phone)
VALUES ('P001', 'yaaa', '1234567890'),
   ('P002', 'jaaa', '4444444444');

INSERT INTO flight (flight_id, flight_name, flight_time, flight_destination,seats_left)
VALUES ('F001', 'Emirates', '12', 'Uganda','100'),
       ('F002', 'Indigo', '15', 'Ghana','100');

INSERT INTO bookings (passenger_id, flight_id, flight_name)
VALUES ('P001', 'F001', 'Indigo'),
       ('P002', 'F002', 'AirIndia');

INSERT INTO service_feedback (passenger_id, flight_id, rating, review_text)
VALUES ('P001', 'F001', 4, 'worst service ever.'),
       ('P002', 'F002', 2, 'Disappointing experience, delayed departure.');
