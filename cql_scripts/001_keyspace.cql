CREATE KEYSPACE IF NOT EXISTS play_api_sparkle WITH
  replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 };