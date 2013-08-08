USE xauth;

DELETE FROM xhome_xauth_role;
INSERT INTO xhome_xauth_role VALUES(1,"ADMIN",1,1,1,NOW(),NOW(),0,2);
INSERT INTO xhome_xauth_role VALUES(2,"MEMBER",2,1,1,NOW(),NOW(),0,2);

DELETE FROM xhome_xauth_user;
INSERT INTO xhome_xauth_user VALUES(1,"xhome","xhome","8c47ae7fa4d9004d32e33389f16dc1a9","MD5","cpf624@126.com",1,1,NOW(),NOW(),0,2);

DELETE FROM xhome_xauth_user_role;
INSERT INTO xhome_xauth_user_role VALUES(1,1,1,1,1,NOW(),NOW(),0,2);
