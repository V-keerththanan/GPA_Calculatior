Create database GPA_Calculator;
use GPA_Calculator;
create table userDetails(
	userName varchar(30) ,
    courseDuration int 
);

create table courseDetails(
	id int auto_increment,
	coursecode char(10),
    courseCredit int not null,
    courseGrade char(2) not null,
    courseYear int not null,
    primary key(id)
);
select * from userDetails;