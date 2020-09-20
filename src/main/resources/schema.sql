CREATE TABLE IF NOT EXISTS `householdmember` (
  `id` int(11) NOT NULL,
  `personNumber` varchar(11) NOT NULL,
  `startDate` date,
  `endDate` date,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE IF NOT EXISTS `expense` (
  `id` int(11) NOT NULL,
  `amount` double,
  `expenseType` varchar(20),
  `otherExpenseDescription` varchar(200),
  `startDate` date,
  `endDate` date,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `income` (
  `id` int(11) NOT NULL,
  `amount` double,
  `incomeType` varchar(20),
  `otherIncomeDescription` varchar(200),
  `startDate` date,
  `endDate` date,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `application` (
  `id` int(11) NOT NULL,
  `startDate` date,
  `endDate` date,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `application_householdmember` (
  `application_id` int(11) NOT NULL,
  `householdmember_id` int(11) NOT NULL,
  CONSTRAINT `app_hm_fk_1` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`),
  CONSTRAINT `app_hm_fk_2` FOREIGN KEY (`householdmember_id`) REFERENCES `householdmember` (`id`)
);

CREATE TABLE IF NOT EXISTS `application_income` (
  `application_id` int(11) NOT NULL,
  `income_id` int(11) NOT NULL,
  CONSTRAINT `app_income_fk_1` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`),
  CONSTRAINT `app_income_fk_2` FOREIGN KEY (`income_id`) REFERENCES `income` (`id`)
);

CREATE TABLE IF NOT EXISTS `application_expense` (
  `application_id` int(11) NOT NULL,
  `expense_id` int(11) NOT NULL,
  CONSTRAINT `app_expense_fk_1` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`),
  CONSTRAINT `app_expense_fk_2` FOREIGN KEY (`expense_id`) REFERENCES `expense` (`id`)
);

CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;
