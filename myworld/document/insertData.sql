--  Client
INSERT INTO `bank`.`tbClient` (`fname`, `lname`, `gender`, `birthday`, `tel`, `add1`, `add2`, `zip`, `email`, `username`, `pw`) VALUES ('Li', 'Huang', 'M', '1985-09-04', '505-224-0333', '2299 Brodhead Rd.', 'Bathlehem,PA', '40292', 'huangli@gmail.com', 'huangli', '1');
INSERT INTO `bank`.`tbClient` (`fname`, `lname`, `gender`, `birthday`, `tel`, `add1`, `add2`, `zip`, `email`, `username`, `pw`) VALUES ('Ben', 'Millar', 'F', '1999-03-02', '511-222-3333', '1355 Main St.', 'New York.NY', '21003', 'ben@gmail.com', 'ben', '2');


-- admin
INSERT INTO `bank`.`tbAdmin` (`adminid`, `username`, `pw`) VALUES ('1', 'admin', '1');

-- account type
INSERT INTO `bank`.`tbAccountType` (`typeid`, `typename`) VALUES ('1', 'checking');
INSERT INTO `bank`.`tbAccountType` (`typeid`, `typename`) VALUES ('2', 'saving');


-- account
INSERT INTO `bank`.`tbAccount` (`cid`, `typeid`, `acnumber`, `balance`) VALUES ('1', '1', '1001', '500');
INSERT INTO `bank`.`tbAccount` (`cid`, `typeid`, `acnumber`, `balance`) VALUES ('1', '2', '1002', '200');
INSERT INTO `bank`.`tbAccount` (`cid`, `typeid`, `acnumber`, `balance`) VALUES ('2', '1', '2001', '300');
INSERT INTO `bank`.`tbAccount` (`cid`, `typeid`, `acnumber`, `balance`) VALUES ('2', '2', '2002', '800');

-- transfer type
INSERT INTO `bank`.`tbTransactionType` (`trtid`, `trtname`) VALUES ('1', 'deposit');
INSERT INTO `bank`.`tbTransactionType` (`trtid`, `trtname`) VALUES ('2', 'withdraw');
INSERT INTO `bank`.`tbTransactionType` (`trtid`, `trtname`) VALUES ('3', 'transferin');
INSERT INTO `bank`.`tbTransactionType` (`trtid`, `trtname`) VALUES ('4', 'transferout');

-- transfer
INSERT INTO `bank`.`tbTransaction` (`aid`, `trtype`, `amount`, `description`) VALUES ('2', '2', '30', 'withdraw 30 at ATM');
INSERT INTO `bank`.`tbTransaction` (`aid`, `trtype`, `amount`,`description`) VALUES ('3', '1', '22', 'deposit 22 from check');
INSERT INTO `bank`.`tbTransaction` (`aid`, `trtype`, `amount`,`description`) VALUES ('4', '3', '44','transfer 44  to 1002');
