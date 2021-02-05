deallocate all;
prepare removeNotification(int) as
  delete from Notifications where notification_id = $1;

execute removeNotification(16);
