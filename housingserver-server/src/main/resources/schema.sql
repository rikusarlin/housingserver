CREATE TABLE IF NOT EXISTS person(
  id int(11) NOT NULL,
  firstName varchar(40),
  lastName varchar(40),
  personNumber varchar(11) NOT NULL,
  birthDate date,
  PRIMARY KEY (id),
  UNIQUE(personNumber)
);

CREATE TABLE IF NOT EXISTS cases (
  id int(11) NOT NULL,
  customer_id int(11) NOT NULL,
  caseState varchar(20),
  PRIMARY KEY (id),
  FOREIGN KEY(customer_id) REFERENCES person(id)
);

CREATE TABLE IF NOT EXISTS application(
  id int(11) NOT NULL,
  case_id int(11) NOT NULL,
  applicant_id int(11) NOT NULL,
  received datetime,
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(applicant_id) REFERENCES person(id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE TABLE IF NOT EXISTS householdmember(
  id int(11) NOT NULL,
  case_id int(11) NOT NULL,
  customer_id int(11) NOT NULL,
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id),
  FOREIGN KEY(customer_id) REFERENCES person(id)
) ;

CREATE TABLE IF NOT EXISTS expense (
  id int(11) NOT NULL,
  case_id int(11) NOT NULL,
  amount double,
  expenseType varchar(20),
  otherExpenseDescription varchar(200),
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE TABLE IF NOT EXISTS income (
  id int(11) NOT NULL,
  case_id int(11) NOT NULL,
  amount double,
  incomeType varchar(20),
  otherIncomeDescription varchar(200),
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 10 INCREMENT BY 1;
