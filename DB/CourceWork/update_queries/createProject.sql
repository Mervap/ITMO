create or replace procedure createProject(in project_name varchar(150),
                                          in project_creator_id int)
  language plpgsql
as
$$
declare
  new_project_id int;
begin
  new_project_id := nextval('ProjectsIdSeq');
  insert into Projects (project_id, name, creator_id)
  values (new_project_id, project_name, project_creator_id);

  insert into ProjectTeamMembers (project_id, user_id, join_date)
  values (new_project_id, project_creator_id, now());
end;
$$;

call createProject('My Comp3', 3);