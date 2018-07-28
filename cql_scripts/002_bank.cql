CREATE TABLE IF NOT EXISTS play_api_sparkle.bank(
  id INT,
  created_by UUID,
  created_at TIMESTAMP,
  full_name TEXT,
  short_name TEXT,
  logo_url TEXT,
  website_url TEXT,
  swift_bic TEXT,
  national_identifier TEXT,
  bank_routing_scheme TEXT,
  bank_routing_address TEXT,
  PRIMARY KEY (id, created_at)
);