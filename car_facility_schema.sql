<-- Precondition: car_facility_system database exists and car_facility_schema exist 

CREATE DATABASE car_facility_system;

CREATE SCHEMA car_facility_system.car_facility_schema;

CREATE TABLE car_facility_schema.cars (
  chassis_number VARCHAR(50) PRIMARY KEY UNIQUE NOT NULL,
  model          VARCHAR(50)
);

CREATE TABLE car_facility_schema.parts (
  id                 SERIAL PRIMARY KEY UNIQUE NOT NULL,
  car_chassis_number VARCHAR(50) REFERENCES car_facility_schema.cars (chassis_number) ON DELETE SET NULL ON UPDATE CASCADE,
  name               VARCHAR(250),
  weight_kg          NUMERIC(10, 3)
);

CREATE TABLE car_facility_schema.pallets (
  id           SERIAL PRIMARY KEY UNIQUE NOT NULL,
  pallet_type  VARCHAR(50),
  total_weight NUMERIC(10, 3)
)

CREATE TABLE car_facility_schema.contains (
  part_id   INT REFERENCES car_facility_schema.parts (id) ON DELETE SET NULL ON UPDATE CASCADE,
  pallet_id INT REFERENCES car_facility_schema.pallets (id) ON DELETE SET NULL ON UPDATE CASCADE,
  PRIMARY KEY (part_id, pallet_id)
);
