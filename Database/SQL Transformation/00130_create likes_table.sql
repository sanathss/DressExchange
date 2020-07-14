
IF NOT EXISTS(SELECT *
                FROM sys.objects
               WHERE TYPE = 'U'
                 AND NAME = 'likes')
BEGIN
   -- Define your columns in your table
   CREATE TABLE dbo.likes
   (      likes_id            NUMERIC(18)  IDENTITY PRIMARY KEY CLUSTERED,
          users_id            NUMERIC(18)  NOT NULL,
          liked_user_id       NUMERIC(18)  NOT NULL)
   ON [DEFAULT]
 
    ALTER TABLE dbo.likes
     ADD CONSTRAINT liker_users_FK FOREIGN KEY (users_id)
         REFERENCES dbo.users(users_id)  
   
    ALTER TABLE dbo.likes
     ADD CONSTRAINT liked_user_FK FOREIGN KEY (liked_user_id)
         REFERENCES dbo.users(users_id)

END
