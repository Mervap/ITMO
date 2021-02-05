deallocate all;
prepare createProjectRole(int, varchar(100), text) as
  insert into Roles (role_id, project_id, name, description)
  values (nextval('RolesIdSeq'), $1, $2, $3);

execute createProjectRole(6, 'My Role', 'Greate role!');
