create table category
(
    category_id   serial,
    category_name varchar(8),
    create_ts     timestamp,
    update_ts     timestamp,
    constraint categories_pk primary key (category_id)
);

create table hp_user
(
    user_id         serial,
    password_digest bytea,
    nick_name       varchar(64),
    email           varchar(64),
    phone           varchar(64),
    tc_accepted     boolean,
    birthday        date,
    gender          char(1),
    real_name       varchar(64),
    address_1       varchar(128),
    address_2       varchar(128),
    city            varchar(128),
    region          varchar(64),
    country         varchar(32),
    zip_code        varchar(16),
    profile_img_url varchar(256),
    organization    varchar(128),
    signature       varchar(256),  -- 个性签名
    create_ts       timestamp,
    update_ts       timestamp,
    constraint user_pk primary key (user_id)
);

create table album
(
    album_id   serial,
    user_id    integer,
    album_name varchar(64),
    create_ts  timestamp,
    update_ts  timestamp,
    constraint album_pk primary key (album_id),
    constraint album_fk1 foreign key (user_id) references hp_user (user_id)
);

create table article
(
    article_id serial,
    title      varchar(64),
    text_body  varchar(1024),
    likes      integer,
    collects   integer,
    -- location
    user_id    integer,
    create_ts  timestamp,
    update_ts  timestamp,
    constraint article_pk primary key (article_id),
    constraint article_fk1 foreign key (user_id) references users (user_id)
);

create table image
(
    img_id              serial,
    position_index      integer,
    article_id          integer,
    external_url        varchar(256),
    external_url_medium varchar(256),
    external_url_small  varchar(256),
    hash_digest         bytea(32),
    status              integer, -- 0: pending, 1: published, 2: inactive, 3 deleted
    create_ts           timestamp,
    update_ts           timestamp,
    constraint image_pk primary key (img_id),
    constraint image_fk1 foreign key (article_id) references article (article_id)
);

create table topic
(
    topic_id  serial,
    tag_name  varchar(16),
    create_ts timestamp,
    update_ts timestamp,
    constraint topic_pk primary key (topic_id)
);

create table article_topic_relation
(
    id         UUID,
    article_id integer,
    topic_id   integer,
    create_ts  timestamp,
    update_ts  timestamp,
    constraint article_topic_relation_pk primary key (id),
    constraint article_topic_relation_fk1 foreign key (article_id) references article (article_id),
    constraint article_topic_relation_fk2 foreign key (topic_id) references topic (topic_id)
);

create table comment (
    comment_id serial,
    text_body varchar(512),
    likes integer,
    article_id integer,
    author_id integer, -- user who writes the comment
    create_ts timestamp,
    update_ts timestamp,
    constraint comment_pk primary key (comment_id),
    constraint comment_fk1 foreign key (article_id) references article (article_id),
    constraint comment_fk2 foreign key (author_id) references hp_user (user_id)
);

create table comment_reply (
    reply_id serial,
    article_id integer,
    comment_id integer,
    text_body varchar(512),
    likes integer,
    replier_id integer,
    create_ts timestamp,
    update_ts timestamp,
    constraint comment_reply_pk primary key (replier_id),
    constraint comment_reply_fk1 foreign key (article_id) references article (article_id),
    constraint comment_reply_fk2 foreign key (replier_id) references hp_user (user_id)
);

create table user_category_relation
(
    id          UUID,
    user_id     integer,
    category_id integer,
    create_ts   timestamp,
    update_ts   timestamp,
    constraint user_category_relation_pk primary key (id),
    constraint user_category_relation_fk1 foreign key (user_id) references hp_user (user_id),
    constraint user_category_relation_fk2 foreign key (category_id) references category (category_id)
);


create table user_following_relation
(
    id                UUID,
    user_id           integer,
    following_user_id integer,
    create_ts         timestamp,
    update_ts         timestamp,
    constraint user_following_relation_pk primary key (id),
    constraint user_following_relation_fk1 foreign key (user_id) references hp_user (user_id),
    constraint user_following_relation_fk2 foreign key (following_user_id) references hp_user (user_id)
);

create table album_article_relations
(
    id         UUID,
    album_id   integer,
    article_id integer,
    create_ts  timestamp,
    update_ts  timestamp,
    constraint album_article_relation_pk primary key (id),
    constraint album_article_relation_fk1 foreign key (album_id) references album (album_id),
    constraint album_article_relation_fk2 foreign key (article_id) references article (article_id)
);

create table article_like_relation
(
    id                               UUID,
    liker_id                         integer,
    article_id                       integer,
    liker_follower_count_approximate integer,
    create_ts                        timestamp,
    update_ts                        timestamp,
    constraint article_like_relation_pk primary key (id),
    constraint article_like_relation_fk1 foreign key (liker_id) references hp_user (user_id),
    constraint article_like_relation_fk2 foreign key (article_id) references article (article_id)
);

create table article_collect_relation
(
    id                                   UUID,
    collector_id                         integer,
    article_id                           integer,
    collector_follower_count_approximate integer,
    create_ts                            timestamp,
    update_ts                            timestamp,
    constraint article_collect_relation_pk primary key (id),
    constraint article_collect_relation_fk1 foreign key (collector_id) references users (user_id),
    constraint article_collect_relation_fk2 foreign key (article_id) references article (article_id)
);