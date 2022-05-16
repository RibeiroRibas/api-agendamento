INSERT INTO USER(name, email, password) VALUES('Profissional', 'profissional@email.com', '$2a$10$n//D1acfPXQxzNqKQlIgn.geqIie.hXPzFWfkPcfn7Xg/cyIpyQu.');
INSERT INTO USER(name, email, password) VALUES('Moderador', 'moderador@email.com', '$2a$10$n//D1acfPXQxzNqKQlIgn.geqIie.hXPzFWfkPcfn7Xg/cyIpyQu.');

INSERT INTO PROFILE(name_profile) VALUES('ROLE_PROFESSIONAL');
INSERT INTO PROFILE(name_profile) VALUES('ROLE_MODERATOR');

INSERT INTO USER_PROFILES(user_id,profiles_id) VALUES(1,1);
INSERT INTO USER_PROFILES(user_id,profiles_id) VALUES(2,2);

--INSERT INTO CLIENT(name,phone) VALUES('Rafael','48995541245');
--INSERT INTO CLIENT(name,phone) VALUES('Ribeiro','48996417323');
--
--INSERT INTO JOB(name,value_of_job,duration_time) VALUES('Unha','10.0','01:00:00');
--INSERT INTO JOB(name,value_of_job,duration_time) VALUES('Fibra','50.0','03:00:00');
