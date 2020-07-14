IF NOT EXISTS(SELECT *
                FROM sys.objects
               WHERE TYPE = 'U'
                 AND NAME = 'dress')
BEGIN
   -- Define your columns in your table
   CREATE TABLE dbo.dress
   (      dress_id            NUMERIC(18)  IDENTITY PRIMARY KEY CLUSTERED,
          users_id            NUMERIC(18)  NOT NULL,
          size                NUMERIC(18)  NOT NULL,
          title               VARCHAR(30)  COLLATE DATABASE_DEFAULT NOT NULL,
          condition           VARCHAR(100) COLLATE DATABASE_DEFAULT NOT NULL,
          insert_datetime     DATETIME     NULL,--NOT NULL,
          insert_user         VARCHAR(20)  NULL,--COLLATE DATABASE_DEFAULT NOT NULL,
          insert_process      VARCHAR(20)  NULL,--COLLATE DATABASE_DEFAULT NOT NULL,
          update_datetime     DATETIME     NULL,
          update_user         VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          update_process      VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL)
   ON [DEFAULT]
   
   ALTER TABLE dbo.dress
     ADD CONSTRAINT dress_users_FK FOREIGN KEY (users_id)
         REFERENCES dbo.users(users_id)

END
