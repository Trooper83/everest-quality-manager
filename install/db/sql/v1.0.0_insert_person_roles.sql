--
-- postgres database setup script for Everest Quality Manager v1.0.0
-- distributed under the GNU Affero General Public License v3
--
-- requires pgcrypto to be added as an extension to the database
--
-- creates roles and an initial user and assigns the admin role to the user
-- replace the user <email> and <password> on line 21 with your own values

INSERT INTO public.role
	(id,version,authority)
VALUES
	(1,0,'ROLE_APP_ADMIN'),
	(2,0,'ROLE_BASIC'),
	(3,0,'ROLE_READ_ONLY'),
	(4,0,'ROLE_PROJECT_ADMIN');

INSERT INTO public.person
	(id,version,password_expired,account_locked,password,account_expired,enabled, email)
VALUES
	(1,0,false,false,'{bcrypt}' || crypt('<password>',gen_salt('bf')),false,true,'<email>');

INSERT INTO public.person_role
	(person_id,role_id)
VALUES
	(1,1);