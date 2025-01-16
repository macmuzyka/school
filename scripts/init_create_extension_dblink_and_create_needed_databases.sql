CREATE EXTENSION IF NOT EXISTS dblink;

DO $$
BEGIN
   RAISE NOTICE 'Creating database: school';
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'school') THEN
      PERFORM dblink_exec('dbname=postgres user=postgres password=postgres', 'CREATE DATABASE school');
   END IF;
   RAISE NOTICE 'Done';

   RAISE NOTICE 'Creating database: informer';
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'informer') THEN
      PERFORM dblink_exec('dbname=postgres user=postgres password=postgres', 'CREATE DATABASE informer');
   END IF;
   RAISE NOTICE 'Done';
END
$$;
