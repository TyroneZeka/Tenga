-- Fix: change rev_reviews.rating from SMALLINT to INTEGER to match Hibernate entity mapping
-- Java int maps to INTEGER (Types#INTEGER), not SMALLINT, so schema validation fails otherwise.

ALTER TABLE rev_reviews
    ALTER COLUMN rating TYPE INTEGER;
