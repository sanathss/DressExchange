IF NOT EXISTS(SELECT *
                FROM sys.objects
               WHERE TYPE = 'U'
                 AND NAME = 'dress_photo')
BEGIN
   -- Define your columns in your table
   CREATE TABLE dbo.dress_photo
   (      dress_photo_id      NUMERIC(18)  IDENTITY PRIMARY KEY CLUSTERED,
          dress_id            NUMERIC(18)  NOT NULL,
          photo_location      VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          photo_bytes         VARBINARY(MAX) NULL,
          photo_encoded       VARCHAR(MAX) NULL,
          dimensions          VARCHAR(10)  NULL,
          insert_datetime     DATETIME     NULL,
          insert_user         VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          insert_process      VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          update_datetime     DATETIME     NULL,
          update_user         VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL,
          update_process      VARCHAR(20)  COLLATE DATABASE_DEFAULT NULL)
   ON [DEFAULT]
   
   ALTER TABLE dbo.dress_photo
     ADD CONSTRAINT dress_photo_dress_FK FOREIGN KEY (dress_id)
         REFERENCES dbo.dress(dress_id)

END