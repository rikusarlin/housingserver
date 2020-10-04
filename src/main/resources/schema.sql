CREATE TABLE IF NOT EXISTS application(
  id int(11) NOT NULL,
  startDate date,
  endDate date,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS householdmember(
  id int(11) NOT NULL,
  application_id int(11) NOT NULL,
  personNumber varchar(11) NOT NULL,
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(application_id) REFERENCES application(id)
) ;

CREATE TABLE IF NOT EXISTS expense (
  id int(11) NOT NULL,
  application_id int(11) NOT NULL,
  amount double,
  expenseType varchar(20),
  otherExpenseDescription varchar(200),
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(application_id) REFERENCES application(id)
);

CREATE TABLE IF NOT EXISTS income (
  id int(11) NOT NULL,
  application_id int(11) NOT NULL,
  amount double,
  incomeType varchar(20),
  otherIncomeDescription varchar(200),
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(application_id) REFERENCES application(id)
);

CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;
