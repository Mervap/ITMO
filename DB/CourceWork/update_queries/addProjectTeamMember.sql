deallocate all;
prepare addProjectTeamMember(int, int) as
  insert into ProjectTeamMembers (project_id, user_id, join_date)
  values ($1, $2, now());

execute addProjectTeamMember(6, 2);
