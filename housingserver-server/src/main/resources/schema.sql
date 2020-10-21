CREATE TABLE IF NOT EXISTS person(
  id integer NOT NULL,
  firstName varchar(80),
  lastName varchar(80),
  personNumber varchar(11) NOT NULL,
  birthDate date,
  gender varchar(20),
  email varchar(80),
  PRIMARY KEY (id),
  UNIQUE(personNumber)
);

CREATE TABLE IF NOT EXISTS cases (
  id integer NOT NULL,
  customer_id integer NOT NULL,
  caseState varchar(20),
  PRIMARY KEY (id),
  FOREIGN KEY(customer_id) REFERENCES person(id)
);

CREATE TABLE IF NOT EXISTS application(
  id integer NOT NULL,
  case_id integer NOT NULL,
  applicant_id integer NOT NULL,
  received timestamp,
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(applicant_id) REFERENCES person(id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE TABLE IF NOT EXISTS householdmember(
  id integer NOT NULL,
  case_id integer NOT NULL,
  customer_id integer NOT NULL,
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id),
  FOREIGN KEY(customer_id) REFERENCES person(id)
) ;

CREATE TABLE IF NOT EXISTS expense (
  id integer NOT NULL,
  case_id integer NOT NULL,
  amount decimal,
  expenseType varchar(20),
  otherExpenseDescription varchar(200),
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE TABLE IF NOT EXISTS income (
  id integer NOT NULL,
  case_id integer NOT NULL,
  amount decimal,
  incomeType varchar(20),
  otherIncomeDescription varchar(200),
  startDate date,
  endDate date,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE TABLE IF NOT EXISTS housingdata (
  id integer NOT NULL,
  case_id integer NOT NULL,
  startDate date,
  endDate date,
  housingdataType varchar(20),
  data text NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(case_id) REFERENCES cases(id)
);

CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 10 INCREMENT BY 1;
