create or replace procedure createNoteGeneric(in note_project_id int,
                                           in note_title varchar(150),
                                           in note_text text,
                                           in note_creator_id int,
                                           in note_related_date timestamp,
                                           in note_place_id int)
  language plpgsql
as
$$
declare
  new_note_id bigint;
begin
  new_note_id := nextval('NotesIdSeq');
  insert into Notes (note_id, title, text, modification_date, creator_id, creation_date, related_date, place_id)
  values (new_note_id, note_title, note_text, now(),
          note_creator_id, now(), note_related_date, note_place_id);

  if note_project_id is not null
  then
    insert into ProjectNotes (note_id, project_id)
    values (new_note_id, note_project_id);
  end if;
end;
$$;

create or replace procedure createNote(in note_title varchar(150),
                                    in note_text text,
                                    in note_creator_id int,
                                    in note_related_date timestamp,
                                    in note_place_id int)
  language sql
as
$$
call createNoteGeneric(null, note_title, note_text, note_creator_id, note_related_date, note_place_id)
$$;


create or replace procedure createProjectNote(in note_project_id int,
                                           in note_title varchar(150),
                                           in note_text text,
                                           in note_creator_id int,
                                           in note_related_date timestamp,
                                           in note_place_id int)
  language sql
as
$$
call createNoteGeneric(note_project_id, note_title, note_text, note_creator_id, note_related_date, note_place_id)
$$;

call createNote('N3xt note1', 'ne21t', 1, null, null);

call createProjectNote(2, 'N3xt note1', 'ne21x', 1, null, 3);

