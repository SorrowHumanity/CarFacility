<-- Precondition: car_facility_system database exists and car_facility_schema exist 

CREATE TABLE car_facility_schema.cars (
  chassis_number VARCHAR(50) PRIMARY KEY UNIQUE NOT NULL,
  model          VARCHAR(50)
);

CREATE TABLE car_facility_schema.parts (
  id                 SERIAL PRIMARY KEY UNIQUE NOT NULL,
  car_chassis_number VARCHAR(50) REFERENCES car_facility_schema.cars (chassis_number) ON DELETE SET NULL ON UPDATE CASCADE,
  name               VARCHAR(250),
  weight_kg          NUMERIC(1)
);