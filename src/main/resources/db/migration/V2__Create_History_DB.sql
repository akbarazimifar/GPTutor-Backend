CREATE TABLE history
(
    id             UUID NOT NULL,
    vk_user_id     UUID NOT NULL,
    last_message   VARCHAR,
    title          VARCHAR,
    system_message VARCHAR,
    PRIMARY KEY (id)
);

alter table if exists history
    add constraint history_vk_user_fk
    foreign key (vk_user_id) references vk_user;