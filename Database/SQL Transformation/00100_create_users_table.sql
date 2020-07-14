IF NOT EXISTS(SELECT *
                FROM sys.objects
               WHERE TYPE = 'U'
                 AND NAME = 'users')
BEGIN
   -- Define your columns in your table
   CREATE TABLE dbo.users
   (      users_id            NUMERIC(18)  IDENTITY PRIMARY KEY CLUSTERED,
          facebook_login      VARCHAR(20)  COLLATE DATABASE_DEFAULT NOT NULL,
          email               VARCHAR(40)  NOT NULL,
          fullname            VARCHAR(50)  NOT NULL,
          size                NUMERIC(18)  NULL,
          suburb_id           NUMERIC(18)  NOT NULL,
          insert_datetime     DATETIME     NULL,
          insert_user         VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          insert_process      VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          update_datetime     DATETIME     NULL,
          update_user         VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          update_process      VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL)
   ON [DEFAULT]

ALTER TABLE dbo.suburb
ADD CONSTRAINT store_suburb_id_FK FOREIGN KEY (suburb_id) 
REFERENCES dbo.suburb(suburb_id)

END

/*
INSERT INTO users(
            facebook_login,
            email,
            fullname,
            size,
            suburb_id)
      SELECT
            '1',
            'sanathss.94@gmail.com',
            'Sanath Samarasekara',
            9,
            1
      UNION
      SELECT
            '2',
            'rusha@gmail.com',
            'Rusha Fernando',
            7,
            1

SELECT * FROM users
*/