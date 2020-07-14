IF NOT EXISTS(SELECT *
                FROM sys.objects
               WHERE TYPE = 'U'
                 AND NAME = 'suburb')
BEGIN
   -- Define your columns in your table
   CREATE TABLE dbo.suburb
   (      suburb_id           NUMERIC(18)  IDENTITY PRIMARY KEY CLUSTERED,
          suburb_name         VARCHAR(25)  NOT NULL)
   ON [DEFAULT]

END

/*
INSERT INTO suburb(
                  suburb_name)
            SELECT 'AUCKLAND'

SELECT * FROM suburb
*/