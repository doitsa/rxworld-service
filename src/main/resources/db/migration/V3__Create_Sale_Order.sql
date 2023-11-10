CREATE TABLE sale_order (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  web_store_id INTEGER NOT NULL,
  date_created DATE,
  date_updated DATE,
  rxworld_id VARCHAR(255),
  doit_id VARCHAR(255),
  CONSTRAINT pk_sale_order PRIMARY KEY (id)
);

ALTER TABLE sale_order ADD CONSTRAINT fk_sale_order__web_store_id FOREIGN KEY (web_store_id) REFERENCES web_store;