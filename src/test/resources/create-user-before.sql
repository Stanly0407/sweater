delete from user_role;
delete from users;

insert into users(id, active, password, username) values
(1, true, '$2a$08$m/s.hAq9rivV1itiFsKLjetFfHxtNSvFwITkOcovwfOsflRKcP4YO', 'Admin'),
(2, true, '$2a$08$5fZVQ/yVgnqK2m0MI7cuxexrsjXAyDDukjDEVz521fcsw/mxzF.Oa', 'Arnold');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');