deallocate all;
prepare getUserRoles(int) as
  select R.project_id, R.role_id, R.name, R.description, UR.grant_date
  from Roles as R natural join UserRoles as UR
  where UR.user_id = $1;

-- User Mervap
execute getUserRoles(1);
-- User Виктор Александрович
execute getUserRoles(2);
-- User Bilbo
execute getUserRoles(3);
-- User Mike Pervotin
execute getUserRoles(4);
