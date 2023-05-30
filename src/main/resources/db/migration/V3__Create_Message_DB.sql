CREATE TABLE message
(
    id                   UUID NOT NULL,
    history_id           UUID NOT NULL,
    content              VARCHAR,
    role                 VARCHAR(16),
    is_error             BOOLEAN,
    is_failed_moderation BOOLEAN,
    PRIMARY KEY (id)
);

alter table if exists message
    add constraint message_vk_user_fk
    foreign key (history_id) references history