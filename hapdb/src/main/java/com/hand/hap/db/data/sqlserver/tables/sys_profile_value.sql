/*


 Date: 06/12/2016 21:55:14 PM
*/

-- ----------------------------
--  Table structure for sys_profile_value
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_profile_value]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_profile_value]
GO
CREATE TABLE [dbo].[sys_profile_value] (
	[PROFILE_VALUE_ID] bigint NOT NULL,
	[PROFILE_ID] nvarchar(32) COLLATE Chinese_PRC_CI_AS NULL,
	[LEVEL_ID] nvarchar(32) COLLATE Chinese_PRC_CI_AS NULL,
	[LEVEL_VALUE] nvarchar(40) COLLATE Chinese_PRC_CI_AS NULL,
	[PROFILE_VALUE] nvarchar(80) COLLATE Chinese_PRC_CI_AS NULL,
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
EXEC sp_addextendedproperty 'MS_Description', N'表ID，主键，供其他表做外键', 'SCHEMA', 'dbo', 'TABLE', 'sys_profile_value', 'COLUMN', 'PROFILE_VALUE_ID'
GO


-- ----------------------------
--  Primary key structure for table sys_profile_value
-- ----------------------------
ALTER TABLE [dbo].[sys_profile_value] ADD
	CONSTRAINT [PK__sys_prof__CA9B2BFB51C5561C] PRIMARY KEY CLUSTERED ([PROFILE_VALUE_ID]) 
	WITH (PAD_INDEX = OFF,
		IGNORE_DUP_KEY = OFF,
		ALLOW_ROW_LOCKS = ON,
		ALLOW_PAGE_LOCKS = ON)
	ON [default]
GO

-- ----------------------------
--  Options for table sys_profile_value
-- ----------------------------
ALTER TABLE [dbo].[sys_profile_value] SET (LOCK_ESCALATION = TABLE)
GO
