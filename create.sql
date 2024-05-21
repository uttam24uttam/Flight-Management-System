CREATE TABLE passenger (
    passenger_id VARCHAR(5),
    passenger_name VARCHAR(30),
    passenger_phone VARCHAR(30),
    constraint pk_passenger PRIMARY KEY (passenger_id)
);

CREATE TABLE flight (
    flight_id VARCHAR(5) ,
    flight_name VARCHAR(30),
    flight_time VARCHAR(2),
    flight_destination VARCHAR(30),
    seats_left INT,
    constraint pk_flight PRIMARY KEY (flight_id)

);

CREATE TABLE bookings (
    passenger_id VARCHAR(5),
    flight_id VARCHAR(5),
    flight_name VARCHAR(30),
    CONSTRAINT fk_pid FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id),
    CONSTRAINT fk_fid FOREIGN KEY (flight_id) REFERENCES flight(flight_id),
    constraint pk_bookings PRIMARY KEY (passenger_id)
);

CREATE TABLE service_feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_id VARCHAR(100) NOT NULL,
    flight_id VARCHAR(100) NOT NULL,
    rating INT,
    review_text VARCHAR(255),
    CONSTRAINT fk_passenger FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id),
    CONSTRAINT fk_flight FOREIGN KEY (flight_id) REFERENCES flight(flight_id)
);
