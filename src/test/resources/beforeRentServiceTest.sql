INSERT INTO BOOK_CATALOG (ID, NAME, AUTHOR, ISBN) VALUES (109,'Contact', 'Carl', '0647639');

INSERT INTO BOOK (ID, PROVIDER, IS_ACTIVE, IS_AVAILABLE, COMMENTS, BOOK_CATALOG_ID) VALUES (100, 'chinnu', '1', '1', 'test', 109);

INSERT INTO RENT (ID, BOOK_ID, USER_ID, ISSUED_DATE, DUE_DATE, IS_CLOSED) VALUES (100, 100, 'vino', '2017-01-01 00:00:00', '2017-01-10 00:00:00', '0');