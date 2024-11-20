CREATE SCHEMA IF NOT EXISTS `mydb`;
USE
`mydb` ;


-- -----------------------------------------------------
-- Table `mydb`.`CALENDAR`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`CALENDAR`
(
    `calendar_id`
    VARCHAR
(
    50
) NOT NULL,
    `jobs_by_calendar_id` VARCHAR
(
    50
),
    `user_by_calendar_id` VARCHAR
(
    50
),
    `calendar_name` VARCHAR
(
    50
) NOT NULL,
    PRIMARY KEY
(
    `calendar_id`
));
