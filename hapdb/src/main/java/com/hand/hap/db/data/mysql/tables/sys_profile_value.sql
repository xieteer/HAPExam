DROP TABLE IF EXISTS SYS_PROFILE_VALUE;
CREATE TABLE `SYS_PROFILE_VALUE` (
  `PROFILE_VALUE_ID`      BIGINT         AUTO_INCREMENT
  COMMENT '表ID，主键，供其他表做外键',
  `PROFILE_ID`            VARCHAR(32) COMMENT '配置文件ID',
  `LEVEL_ID`              VARCHAR(32) COMMENT '层次ID',
  `LEVEL_VALUE`           VARCHAR(40) COMMENT '层次值',
  `PROFILE_VALUE`         VARCHAR(80) COMMENT '配置文件值',
  `OBJECT_VERSION_NUMBER` DECIMAL(20, 0) DEFAULT 1,
  `REQUEST_ID`            BIGINT         DEFAULT -1,
  `PROGRAM_ID`            BIGINT         DEFAULT -1,
  `CREATION_DATE`         TIMESTAMP       DEFAULT now(),
  `CREATED_BY`            DECIMAL(20, 0) DEFAULT -1,
  `LAST_UPDATED_BY`       DECIMAL(20, 0) DEFAULT -1,
  `LAST_UPDATE_DATE`      TIMESTAMP       DEFAULT now(),
  `LAST_UPDATE_LOGIN`     DECIMAL(20, 0),
  `ATTRIBUTE_CATEGORY`    VARCHAR(30),
  `ATTRIBUTE1`            VARCHAR(240),
  `ATTRIBUTE2`            VARCHAR(240),
  `ATTRIBUTE3`            VARCHAR(240),
  `ATTRIBUTE4`            VARCHAR(240),
  `ATTRIBUTE5`            VARCHAR(240),
  `ATTRIBUTE6`            VARCHAR(240),
  `ATTRIBUTE7`            VARCHAR(240),
  `ATTRIBUTE8`            VARCHAR(240),
  `ATTRIBUTE9`            VARCHAR(240),
  `ATTRIBUTE10`           VARCHAR(240),
  `ATTRIBUTE11`           VARCHAR(240),
  `ATTRIBUTE12`           VARCHAR(240),
  `ATTRIBUTE13`           VARCHAR(240),
  `ATTRIBUTE14`           VARCHAR(240),
  `ATTRIBUTE15`           VARCHAR(240),
  PRIMARY KEY (`PROFILE_VALUE_ID`)
)
  COMMENT = '配置文件值';
ALTER TABLE `SYS_PROFILE_VALUE`
  CHANGE `LEVEL_ID` `LEVEL_ID` VARCHAR(32) BINARY;
ALTER TABLE `SYS_PROFILE_VALUE`
  CHANGE `LEVEL_VALUE` `LEVEL_VALUE` VARCHAR(40) BINARY;
ALTER TABLE `SYS_PROFILE_VALUE`
  CHANGE `PROFILE_ID` `PROFILE_ID` VARCHAR(32) BINARY;
ALTER TABLE `SYS_PROFILE_VALUE`
  CHANGE `PROFILE_VALUE` `PROFILE_VALUE` VARCHAR(80) BINARY;
