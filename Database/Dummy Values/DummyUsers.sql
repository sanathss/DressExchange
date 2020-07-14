USE dress_exchange
GO

DECLARE @d_current_datetime DATETIME,
        @v_user             VARCHAR(20),
        @v_process          VARCHAR(20)

SELECT @d_current_datetime = GETDATE(),
       @v_user = HOST_NAME(),
       @v_process = 'dummy_listing'

INSERT INTO [dbo].[users]
           ([facebook_login],
            [email],
            [fullname],
            [size],
			[suburb_id])
     VALUES
           ('0987654321',
           'sanathss.94@gmail.com',
           'Sanath Samarasekara',
           9,
		   1)

SELECT *
  FROM users

--INSERT INTO [dbo].[users]
--           ([facebook_login],
--            [email],
--            [fullname],
--            [size],
--            [insert_datetime],
--            [insert_user],
--            [insert_process])
--     VALUES
--           ('0987654321',
--           'sanathss.94@gmail.com',
--           'Sanath Samarasekara',
--           9,
--           @d_current_datetime,
--           @v_user,
--           @v_process)

--INSERT INTO [dbo].[users]
--           ([facebook_login],
--            [email],
--            [fullname],
--            [size],
--            [insert_datetime],
--            [insert_user],
--            [insert_process])
--     VALUES
--           ('1234567890',
--           '',
--           'Rushini Fernando',
--           7,
--           @d_current_datetime,
--           @v_user,
--           @v_process)
GO

