Uzonna Alexander
101233844
COMP 3005 Assignment 3, Question 1

This is the code to create the table.

CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    enrollment_date DATE
);


Then I used the given code to add a few entries.

SETUP:
My code was written in IntelliJ (Java), so it should run if you have the dependencies listed
in the course video. 


YouTube Link: https://youtu.be/Valuvgbbeis
