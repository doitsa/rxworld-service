CREATE TABLE rxworld_config (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  client_id VARCHAR(255),
  client_secret VARCHAR(255),
  current_bearer_token VARCHAR,
  token_expires_at VARCHAR(255),
  CONSTRAINT pk_rxworld_config PRIMARY KEY (id)
);