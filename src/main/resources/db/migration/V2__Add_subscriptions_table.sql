create table user_subscriptions (
  channel_id bigint not null references users,
  subscriber_id  bigint not null references users,
  primary key (channel_id, subscriber_id)
);
