/*


 Date: 06/12/2016 21:55:03 PM
*/

-- ----------------------------
--  Table structure for sys_preferences
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_preferences]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_preferences]
GO
CREATE TABLE [dbo].[sys_preferences] (
	[PREFERENCES_ID] bigint NOT NULL,
	[PREFERENCES] nvarchar(30) COLLATE Chinese_PRC_CI_AS NULL,
	[PREFERENCES_LEVEL] decimal(20,0) NULL,
	[PREFERENCES_VALUE] nvarchar(80) COLLATE Chinese_PRC_CI_AS NULL,
	[USER_ID] bigint NULL,
	[OBJECT_VERSION_NUMBER] decimal(20,0) NULL,
	[REQUEST_ID] bigint NULL,
	[PROGRAM_ID] bigint NULL,
	[CREATION_DATE] datetime2(0) NULL,
	[CREATED_BY] decimal(20,0) NULL,
	[LAST_UPDATED_BY] decimal(20,0) NULL,
	[LAST_UPDATE_DATE] datetime2(0) NULL,
	[LAST_UPDATE_LOGIN] decimal(20,0) NULL,
	[ATTRIBUTE_CATEGORY] varchar(30) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE1] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE2] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE3] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE4] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE5] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE6] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE7] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE8] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE9] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE10] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE11] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE12] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE13] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE14] varchar(240) COLLATE Chinese_PRC_CI_AS NULL,
	[ATTRIBUTE15] varchar(240) COLLATE Chinese_PRC_CI_AS NULL
)
ON [PRIMARY]
GO
EXEC sp_addextendedproperty 'MS_Description', N'表ID，主键，供其他表做外键', 'SCHEMA', 'dbo', 'TABLE', 'sys_preferences', 'COLUMN', 'PREFERENCES_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', N'逻辑分类：10 DSIS首选项, 20 MWS首选项', 'SCHEMA', 'dbo', 'TABLE', 'sys_preferences', 'COLUMN', 'PREFERENCES_LEVEL'
GO
EXEC sp_addextendedproperty 'MS_Description', N'账号ID', 'SCHEMA', 'dbo', 'TABLE', 'sys_preferences', 'COLUMN', 'USER_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', N'行版本号，用来处理锁', 'SCHEMA', 'dbo', 'TABLE', 'sys_preferences', 'COLUMN', 'OBJECT_VERSION_NUMBER'
GO
EXEC sp_addextendedproperty 'MS_Description', N'对Record最后一次操作的系统内部请求id', 'SCHEMA', 'dbo', 'TABLE', 'sys_preferences', 'COLUMN', 'REQUEST_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', N'对Record最后一次操作的系统内部程序id', 'SCHEMA', 'dbo', 'TABLE', 'sys_preferences', 'COLUMN', 'PROGRAM_ID'
GO


-- ----------------------------
--  Primary key structure for table sys_preferences
-- ----------------------------
ALTER TABLE [dbo].[sys_preferences] ADD
	CONSTRAINT [PK__sys_pref__F05B708893C13DE4] PRIMARY KEY CLUSTERED ([PREFERENCES_ID]) 
	WITH (PAD_INDEX = OFF,
		IGNORE_DUP_KEY = OFF,
		ALLOW_ROW_LOCKS = ON,
		ALLOW_PAGE_LOCKS = ON)
	ON [default]
GO

-- ----------------------------
--  Options for table sys_preferences
-- ----------------------------
ALTER TABLE [dbo].[sys_preferences] SET (LOCK_ESCALATION = TABLE)
GO
