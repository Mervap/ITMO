deallocate all;
prepare grantRoleToProjectMember(int, int, int) as
  insert into UserRoles (user_id, role_id, project_id, grant_date)
  values ($1, $2, $3, now());

execute grantRoleToProjectMember(3, 10, 6);
