/*


 Date: 06/12/2016 21:53:44 PM
*/

-- ----------------------------
--  Table structure for sys_job_running_info
-- ----------------------------
IF EXISTS(SELECT *
          FROM sys.all_objects
          WHERE object_id = OBJECT_ID('[dbo].[sys_job_running_info]') AND type IN ('U'))
  DROP TABLE [dbo].[sys_job_running_info]
GO
CREATE TABLE [dbo].[sys_job_running_info] (
  [JOB_RUNNING_INFO_ID]   BIGINT                                  NOT NULL,
  [JOB_NAME]              NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [JOB_GROUP]             NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [JOB_RESULT]            NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [JOB_STATUS]            NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [JOB_STATUS_MESSAGE]    NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [TRIGGER_NAME]          NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [TRIGGER_GROUP]         NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [PREVIOUS_FIRE_TIME]    DATETIME2(0)                            NULL,
  [FIRE_TIME]             DATETIME2(0)                            NULL,
  [NEXT_FIRE_TIME]        DATETIME2(0)                            NULL,
  [REFIRE_COUNT]          BIGINT                                  NULL,
  [FIRE_INSTANCE_ID]      NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [SCHEDULER_INSTANCE_ID] NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [SCHEDULED_FIRE_TIME]   DATETIME2(0)                            NULL,
  [EXECUTION_SUMMARY]     NVARCHAR(255) COLLATE Chinese_PRC_CI_AS NULL,
  [OBJECT_VERSION_NUMBER] DECIMAL(20, 0)                          NULL,
  [REQUEST_ID]            BIGINT                                  NULL,
  [PROGRAM_ID]            BIGINT                                  NULL,
  [CREATED_BY]            BIGINT                                  NULL,
  [CREATION_DATE]         DATETIME2(0)                            NULL,
  [LAST_UPDATED_BY]       BIGINT                                  NULL,
  [LAST_UPDATE_DATE]      DATETIME2(0)                            NULL,
  [LAST_UPDATE_LOGIN]     DECIMAL(20, 0)                          NULL,
  [ATTRIBUTE_CATEGORY]    VARCHAR(30) COLLATE Chinese_PRC_CI_AS   NULL,
  [ATTRIBUTE1]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE2]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE3]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE4]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE5]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE6]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE7]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE8]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE9]            VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE10]           VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE11]           VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE12]           VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE13]           VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE14]           VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTRIBUTE15]           VARCHAR(255) COLLATE Chinese_PRC_CI_AS  NULL
)
ON [PRIMARY]
GO


-- ----------------------------
--  Primary key structure for table sys_job_running_info
-- ----------------------------
ALTER TABLE [dbo].[sys_job_running_info]
  ADD
  CONSTRAINT [PK__sys_job___CD9A0A62AFDDDAF2] PRIMARY KEY CLUSTERED ([JOB_RUNNING_INFO_ID])
    WITH (PAD_INDEX = OFF,
      IGNORE_DUP_KEY = OFF,
      ALLOW_ROW_LOCKS = ON,
      ALLOW_PAGE_LOCKS = ON)
    ON [default]
GO

-- ----------------------------
--  Options for table sys_job_running_info
-- ----------------------------
ALTER TABLE [dbo].[sys_job_running_info]
  SET ( LOCK_ESCALATION = TABLE )
GO

