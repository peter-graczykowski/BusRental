create table ACCOUNT (
ID BIGINT PRIMARY KEY, 
ACTIVE SMALLINT NOT NULL,
ANSWER VARCHAR(100) NOT NULL,
AUTHORIZED SMALLINT NOT NULL,
CREATED_AT TIMESTAMP,
LOGIN VARCHAR(20) NOT NULL, CONSTRAINT UNIQUE_LOGIN unique (LOGIN),
MODIFIED_AT TIMESTAMP,
"NAME" VARCHAR(60) NOT NULL,
PASSWORD VARCHAR(64) NOT NULL, 
PHONE_NUMBER VARCHAR(13) NOT NULL,
QUESTION VARCHAR(100) NOT NULL,
SURNAME	VARCHAR(100) NOT NULL,
VERSION	INTEGER NOT NULL,
LEVEL_OF_ACCESS	VARCHAR(31) NOT NULL,
ACCOUNT_CREATOR BIGINT,
CONSTRAINT CCUNTCCOUNTCREATOR FOREIGN KEY (ACCOUNT_CREATOR) REFERENCES ACCOUNT(ID),
MODIFIED_BY BIGINT,
CONSTRAINT ACCOUNTMODIFIED_BY FOREIGN KEY (MODIFIED_BY) REFERENCES ACCOUNT(ID));

create table BUS (
ID BIGINT PRIMARY KEY, 
BUS_NAME VARCHAR(100) NOT NULL,
CREATED_AT TIMESTAMP NOT NULL,
PLATE_NUMBER VARCHAR(9) NOT NULL, 
CONSTRAINT UNIQUE_PLATE_NR unique (PLATE_NUMBER),
SEATS INTEGER NOT NULL,
VERSION	INTEGER NOT NULL,
BUS_CREATOR BIGINT,
CONSTRAINT FK_BUS_BUS_CREATOR FOREIGN KEY (BUS_CREATOR) REFERENCES ACCOUNT(ID),
MODIFIED_BY BIGINT,
CONSTRAINT FK_BUS_MODIFIED_BY FOREIGN KEY (MODIFIED_BY) REFERENCES ACCOUNT(ID),
MODIFIED_AT TIMESTAMP,
ACTIVE SMALLINT NOT NULL);

create table ORDERS (
ID BIGINT PRIMARY KEY, 
CREATED_AT TIMESTAMP NOT NULL,
MODIFIED_AT TIMESTAMP,
ORDER_START_DATE DATE NOT NULL,
VERSION	INTEGER NOT NULL,
ORDER_END_DATE DATE NOT NULL,
ORDERED_BUS BIGINT,
CONSTRAINT ORDERS_ORDERED_BUS FOREIGN KEY (ORDERED_BUS) REFERENCES BUS(ID),
ORDER_CREATOR BIGINT,
CONSTRAINT ORDERSORDERCREATOR FOREIGN KEY (ORDER_CREATOR) REFERENCES ACCOUNT(ID),
CONSTRAINT UNIQ_CLIENT_START unique (ORDER_CREATOR, ORDER_START_DATE),
CONSTRAINT UNIQ_CLIENT_END unique (ORDER_CREATOR, ORDER_END_DATE),
CONSTRAINT UNIQ_BUS_START unique (ORDERED_BUS, ORDER_START_DATE),
CONSTRAINT UNIQ_BUS_END unique (ORDERED_BUS, ORDER_END_DATE),
MODIFIED_BY BIGINT,
CONSTRAINT ORDERS_MODIFIED_BY FOREIGN KEY (MODIFIED_BY) REFERENCES ACCOUNT(ID));

CREATE VIEW AUTH_TABLE AS 
SELECT LOGIN, PASSWORD, LEVEL_OF_ACCESS FROM ACCOUNT WHERE active>0 AND authorized>0 AND NOT level_of_access  = 'access.level.newaccount';

