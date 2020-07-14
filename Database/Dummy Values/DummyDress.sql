USE dress_exchange
GO

DECLARE @d_current_date     DATETIME,
        @v_user             VARCHAR(20),
        @v_process          VARCHAR(20),
        @sanath             NUMERIC(18),
        @rushini            NUMERIC(18)

SELECT @d_current_date = GETDATE(),
       @v_user = HOST_NAME(),
       @v_process = 'dummy_listing'

select @sanath = users_id
  From dbo.users
  where facebook_login = '0987654321'

select @rushini = users_id
  From dbo.users
  where facebook_login = 'RushiniF'

INSERT INTO [dbo].[dress]
           (users_id,
            size,
			title,
            condition,
            insert_datetime,
            insert_user,
            insert_process)
     VALUES
           (@sanath,
           9,
		   'RedDress',
           'this is a red dress',
           @d_current_date,
           @v_user,
           @v_process)
/*
select @rushini = users_id
  From dbo.users
  where facebook_login = 'RushiniF'

INSERT INTO [dbo].[dress]
           (users_id,
            size,
			title,
            condition,
            insert_datetime,
            insert_user,
            insert_process)
     VALUES
           (@rushini,
           9,
		   'BlackDress',
           'this is a black dress',
           @d_current_date,
           @v_user,
           @v_process)
		   */
GO

select * from dress