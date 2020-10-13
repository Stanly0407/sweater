delete from message;
delete from hibernate_sequence;

insert into message(id, text, tag, user_id) values
(1, 'first', 'my-tag', 1),
(2, 'second', 'more', 1),
(3, 'third', 'my-tag', 1),
(4, 'fourth', 'another', 1);


--alter table message auto_increment 10;
insert into hibernate_sequence values (10);

-- alter sequence hibernate_sequence restart with 10;