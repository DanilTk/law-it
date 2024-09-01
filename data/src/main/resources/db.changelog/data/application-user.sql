INSERT INTO application_user (uuid,
							  created_at,
							  idp_sub,
							  email,
							  is_idp_user)
VALUES ('7f9c9b9a-4e5f-4d6c-8d7e-1a2b3c4d5e6f',
		current_timestamp,
		'system_idp',
		'system@email.com',
		false);