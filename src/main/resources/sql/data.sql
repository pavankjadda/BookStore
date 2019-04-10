insert into address_type values(5001,'Home');
insert into  address values(3001,'STE 350','Falls Church','VA','3130 Fairview Park Dr','22043',5001);

insert into  author values(2001, 'john.david@gmail.com','John','David','905.456.1792');
insert into  author values(2002, 'steve.oliver@gmail.com','Steve','Oliver','905.456.1234');
insert into  author_address values(2001, 3001);
insert into  author_address values(2002, 3001);


insert into  book values(1001, 19.99,250,'Java12 Essentials');
insert into  book_author values(1001, 2001);
insert into  book_author values(1001, 2002);

