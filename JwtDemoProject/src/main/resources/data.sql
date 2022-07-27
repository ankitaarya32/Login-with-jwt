insert into role("ROLENAME","DESCRIPTION") values ('ADMIN','Have all admin privileges');
insert into role("ROLENAME","DESCRIPTION") values ('USER','Have all user privileges');
insert into user("USERNAME","PASSWORD") values('Ankit','$2a$12$y0jbNfK7X5OMTFPvFwsaNu7iTCGPzPSMVJ7lKqd5sl.Hby3N492XK');
insert into user_role("USER_ID","ROLE_ID") values(1,1);