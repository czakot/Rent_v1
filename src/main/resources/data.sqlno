use registration;
-- insert into blogger (age, name) values(21,'Gyula');
-- insert into blogger (age, name) values(52,'Toma');
insert into blogger (age, name) 
    select * from (select 21,'Gyula') as tmp
        where not exists(select * from blogger where name = 'Gyula');
