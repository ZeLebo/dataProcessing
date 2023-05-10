CREATE TABLE USERS (
                       ID TEXT UNIQUE PRIMARY KEY NOT NULL,
                       NAME VARCHAR(50),
                       SURNAME VARCHAR(50),
                       GENDER TEXT,
                       MOTHER_ID TEXT REFERENCES USERS(ID),
                       FATHER_ID TEXT REFERENCES USERS(ID),
                       WIFE_ID TEXT REFERENCES USERS(ID),
                       HUSBAND_ID TEXT REFERENCES USERS(ID)
);

CREATE TABLE SONS_TABLE (
                            SON_ID TEXT UNIQUE REFERENCES USERS(ID),
                            PARENT_ID TEXT REFERENCES USERS(ID)
);

CREATE TABLE DAUGHTERS_TABLE (
                                 DAUGHTER_ID TEXT UNIQUE REFERENCES USERS(ID),
                                 PARENT_ID TEXT REFERENCES USERS(ID)
);

CREATE TABLE BROTHERS_TABLE (
                                BROTHER_ID TEXT REFERENCES USERS(ID),
                                PERSON_ID TEXT REFERENCES USERS(ID)
);

CREATE TABLE SISTERS_TABLE(
                              SISTER_ID TEXT REFERENCES USERS(ID),
                              PERSON_ID TEXT REFERENCES USERS(ID)
);
