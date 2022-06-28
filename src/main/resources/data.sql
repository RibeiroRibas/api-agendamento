--INSERT INTO USER(name, email, password) VALUES('Profissional', 'profissional@email.com', '$2a$10$n//D1acfPXQxzNqKQlIgn.geqIie.hXPzFWfkPcfn7Xg/cyIpyQu.');
INSERT INTO ADDRESS(street,number,zip_code,city,state,country) VALUES('rua manoel candido de farias',60,'88131604','palhoca','SC','BR');

INSERT INTO PROFILES(name_profile) VALUES('ROLE_PROFISSIONAL');
INSERT INTO PROFILES(name_profile) VALUES('ROLE_CLIENTE');

INSERT INTO USER(name, email, password,phone,address_id) VALUES('Patricia Forkamp', 'profissional@email.com', '$2a$10$n//D1acfPXQxzNqKQlIgn.geqIie.hXPzFWfkPcfn7Xg/cyIpyQu.','48996417323',1);
INSERT INTO USER_PROFILES(user_id,profiles_id) VALUES(1,1);

INSERT INTO COMPANY(cnpj,name,user_id,address_id) VALUES('20643340000148','Beauty Style',1,1);

INSERT INTO JOB(name,value_of_job,duration_time,company_id) VALUES('pintar a unha','25.0','00:30:00',1);
INSERT INTO JOB(name,value_of_job,duration_time,company_id) VALUES('colocação','150.0','03:00:00',1);

INSERT INTO CLIENT(name,phone,user_id,company_id)VALUES('rafa','456123456',0,1);

--INSERT INTO EVENT(client_id,event_date,start_time,end_time,value_event,status_pagamento,company_id) VALUES(1,'2022-06-03','07:30:00','08:00:00','25.0','NAORECEBIDO',1);
--INSERT INTO EVENT(client_id,event_date,start_time,end_time,value_event,status_pagamento,company_id) VALUES(1,'2022-06-03','08:00:00','08:30:00','25.0','NAORECEBIDO',1);
--INSERT INTO EVENT_JOBS(event_id,job_id)VALUES(1,1);
--INSERT INTO EVENT_JOBS(event_id,job_id)VALUES(2,2);

INSERT INTO EXPENSE(description,price,expense_date,category,repeat_or_not,company_id) VALUES('esmale vermelho','25.0','2022-05-20','ESMALTE','NREPEAT',1);

INSERT INTO CATEGORY(name,company_id) VALUES('ALUGUEL',1);
INSERT INTO CATEGORY(name,company_id) VALUES('AGUA',1);
