DROP TABLE IF EXISTS SYS_ATTACHMENT;
CREATE TABLE `SYS_ATTACHMENT` (
  `ATTACHMENT_ID`         BIGINT         AUTO_INCREMENT
  COMMENT '表ID，主键，供其他表做外键',
  `CATEGORY_ID`           BIGINT COMMENT '分类ID',
  `NAME`                  VARCHAR(40) COMMENT '附件名称',
  `SOURCE_TYPE`           VARCHAR(30) COMMENT '对应业务类型',
  `SOURCE_KEY`            VARCHAR(30) COMMENT '业务主键',
  `STATUS`                VARCHAR(1) COMMENT '状态',
  `START_ACTIVE_DATE`     DATETIME COMMENT '开始生效日期',
  `END_ACTIVE_DATE`       DATETIME COMMENT '失效时间',
  `OBJECT_VERSION_NUMBER` DECIMAL(20, 0) DEFAULT 1
  COMMENT '行版本号，用来处理锁',
  `REQUEST_ID`            BIGINT         DEFAULT -1
  COMMENT '对Record最后一次操作的系统内部请求id',
  `PROGRAM_ID`            BIGINT         DEFAULT -1
  COMMENT '对Record最后一次操作的系统内部程序id',
  `CREATION_DATE`         DATETIME       DEFAULT now(),
  `CREATED_BY`            DECIMAL(20, 0) DEFAULT -1,
  `LAST_UPDATED_BY`       DECIMAL(20, 0) DEFAULT -1,
  `LAST_UPDATE_DATE`      DATETIME       DEFAULT now(),
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
  PRIMARY KEY (`ATTACHMENT_ID`),
  KEY `SYS_ATTACHMENT_N1`(`CATEGORY_ID`),
  KEY `SYS_ATTACHMENT_N2`(`SOURCE_TYPE`, `SOURCE_KEY`)
)
  COMMENT = '附件表';
ALTER TABLE `SYS_ATTACHMENT`
  CHANGE `STATUS` `STATUS` VARCHAR(1) BINARY;
ALTER TABLE `SYS_ATTACHMENT`
  CHANGE `SOURCE_TYPE` `SOURCE_TYPE` VARCHAR(30) BINARY;
ALTER TABLE `SYS_ATTACHMENT`
  CHANGE `SOURCE_KEY` `SOURCE_KEY` VARCHAR(30) BINARY;
ALTER TABLE `SYS_ATTACHMENT`
  CHANGE `NAME` `NAME` VARCHAR(40) BINARY;

