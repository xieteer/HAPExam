CREATE TABLE SYS_USER
(
  USER_ID               NUMBER               NOT NULL,
  USER_TYPE             VARCHAR2(30),
  USER_NAME             VARCHAR2(40),
  PASSWORD_ENCRYPTED    VARCHAR2(40)         NOT NULL,
  EMAIL                 VARCHAR2(150),
  PHONE                 VARCHAR2(40),
  START_ACTIVE_DATE     DATE,
  END_ACTIVE_DATE       DATE,
  STATUS                VARCHAR2(30),
  OBJECT_VERSION_NUMBER NUMBER DEFAULT 1,
  REQUEST_ID            NUMBER DEFAULT -1,
  PROGRAM_ID            NUMBER DEFAULT -1,
  CREATION_DATE         DATE DEFAULT sysdate NOT NULL,
  CREATED_BY            NUMBER DEFAULT -1    NOT NULL,
  LAST_UPDATED_BY       NUMBER DEFAULT -1    NOT NULL,
  LAST_UPDATE_DATE      DATE DEFAULT sysdate NOT NULL,
  LAST_UPDATE_LOGIN     NUMBER,
  ATTRIBUTE_CATEGORY    VARCHAR2(30),
  ATTRIBUTE1            VARCHAR2(240),
  ATTRIBUTE2            VARCHAR2(240),
  ATTRIBUTE3            VARCHAR2(240),
  ATTRIBUTE4            VARCHAR2(240),
  ATTRIBUTE5            VARCHAR2(240),
  ATTRIBUTE6            VARCHAR2(240),
  ATTRIBUTE7            VARCHAR2(240),
  ATTRIBUTE8            VARCHAR2(240),
  ATTRIBUTE9            VARCHAR2(240),
  ATTRIBUTE10           VARCHAR2(240),
  ATTRIBUTE11           VARCHAR2(240),
  ATTRIBUTE12           VARCHAR2(240),
  ATTRIBUTE13           VARCHAR2(240),
  ATTRIBUTE14           VARCHAR2(240),
  ATTRIBUTE15           VARCHAR2(240)
);
COMMENT ON TABLE SYS_USER IS '用户表';
COMMENT ON COLUMN SYS_USER.USER_ID IS '表ID，主键，供其他表做外键';
COMMENT ON COLUMN SYS_USER.USER_TYPE IS '用户类型';
COMMENT ON COLUMN SYS_USER.USER_NAME IS '用户名';
COMMENT ON COLUMN SYS_USER.PASSWORD_ENCRYPTED IS '加密过的密码';
COMMENT ON COLUMN SYS_USER.EMAIL IS '邮箱地址';
COMMENT ON COLUMN SYS_USER.PHONE IS '电话号码';
COMMENT ON COLUMN SYS_USER.START_ACTIVE_DATE IS '有效期从';
COMMENT ON COLUMN SYS_USER.END_ACTIVE_DATE IS '有效期至';
COMMENT ON COLUMN SYS_USER.STATUS IS '状态';

ALTER TABLE SYS_USER
  ADD CONSTRAINT SYS_USER_PK PRIMARY KEY (USER_ID);

CREATE SEQUENCE SYS_USER_S START WITH 10001;


