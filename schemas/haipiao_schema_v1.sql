-- 分类表
create table category
(
    category_id   serial,     -- 主键
    category_name varchar(8), -- 分类名，例如：旅游，美食，留学
    type          varchar(8), -- 分类类型，例如：热门（hot），其他（misc）等
    create_ts     timestamp,  -- 行插入时间戳
    update_ts     timestamp,  -- 行最近一次修改时间戳
    constraint categories_pk primary key (category_id)
);

-- 用户表
create table hp_user
(
    user_id               serial,       -- 主键
    password_digest       bytea,        -- 用户密码sha256
    nick_name             varchar(64),  -- 用户昵称
    email                 varchar(64),  -- 用户邮箱
    phone                 varchar(64),  -- 用户手机号
    tc_accepted           boolean,      -- 当前用户是否接受最新的用户条款（T&C: Term & Condition）
    birthday              date,         -- 用户生日
    gender                char(1),      -- 性别代码: M（男），F（女），U（未知）
    real_name             varchar(64),  -- 用后真实姓名
    address_1             varchar(128), -- 地址（第一行）
    address_2             varchar(128), -- 地址（第二行）
    city                  varchar(128), -- 城市
    region                varchar(64),  -- 省/州
    country               varchar(32),  -- 国家
    zip_code              varchar(16),  -- 邮编
    profile_img_url       varchar(256), -- 用户头像链接
    profile_img_url_small varchar(256), -- 用户头像缩略图链接
    organization          varchar(128), -- 用户所在组织（公司，学校）
    signature             varchar(256), -- 个性签名
    role                  int,          -- 用户类型 0（Admin）， 1（Customer）
    create_ts             timestamp,
    update_ts             timestamp,
    constraint user_pk primary key (user_id)
);

create table user_privacy_settings
(
    user_id         serial,
    comment_setting int,
    mention_setting int,
    contact_setting int,
    weibo_setting   int,
    constraint user_privacy_settings_pk primary key (user_id),
    constraint user_privacy_settings_fk foreign key (user_id) references hp_user (user_id)
);

create index hp_user_index0 ON hp_user (phone);

-- 持久话会话表： 用户登录后会获得一个session token。移动端可缓存，30天有效。
create table user_session
(
    session_id       serial,  -- 主键
    user_id          integer, -- session所有者id
    selector         bytea,   -- 关于selector/validator的定义请参照：https://paragonie.com/blog/2017/02/split-tokens-token-based-authentication-protocols-without-side-channels
    validator_digest bytea,
    create_ts        timestamp,
    update_ts        timestamp,
    constraint user_session_pk primary key (user_id),
    constraint user_session_fk foreign key (user_id) references hp_user (user_id)
);

create index user_session_index0 ON user_session (selector);

-- 用户分组表
create table user_group
(
    group_id  serial,  -- 主键
    owner_id  integer, -- 分组创建者的id
    name      integer, -- 分组名
    create_ts timestamp,
    update_ts timestamp,
    constraint user_group_pk primary key (group_id),
    constraint user_group_fk foreign key (owner_id) references hp_user (user_id)
);

-- 专辑表
create table album
(
    album_id   serial,      -- 主键
    user_id    integer,     -- 专辑所有者用户id
    album_name varchar(64), -- 专辑名
    create_ts  timestamp,
    update_ts  timestamp,
    constraint album_pk primary key (album_id),
    constraint album_fk1 foreign key (user_id) references hp_user (user_id)
);

-- 文章表
create table article
(
    article_id serial,        -- 主键
    title      varchar(64),   -- 文章标题
    text_body  varchar(1024), -- 文章正文
    type       integer,       -- 文章类型 0: 只含图像, 1: 含视频
    likes      integer,       -- 文章获得点赞数
    collects   integer,       -- 文章被收藏次数
    shares     integer,       -- 文章被分享次数
    -- TODO: location
    user_id    integer,       -- 文章作者用户id
    status     integer,       -- 文章状态 0: pending, 1: published, 2: inactive, 3 deleted
    create_ts  timestamp,
    update_ts  timestamp,
    constraint article_pk primary key (article_id),
    constraint article_fk1 foreign key (user_id) references hp_user (user_id)
);

-- 图像meta信息表
create table image
(
    img_id              serial,       -- 主键
    position_index      integer,      -- 图片在文章中位置：一片文章可配9张图，position_index取0-8
    article_id          integer,      -- 图片所在文章id
    external_url        varchar(256), -- OSS路径
    external_url_large  varchar(256), -- OSS原图路径（TODO：似乎不需要）
    external_url_medium varchar(256), -- OSS中图路径
    external_url_small  varchar(256), -- OSS缩略图路径
    hash_digest         bytea,        -- 图像hash值
    status              integer,      -- 图像状态 0: pending, 1: published, 2: inactive, 3 deleted
    create_ts           timestamp,
    update_ts           timestamp,
    constraint image_pk primary key (img_id),
    constraint image_fk1 foreign key (article_id) references article (article_id)
);

-- 标签表：图像上可加标签。标签上可有文字。
create table tag
(
    tag_id     serial,      -- 主键
    img_id     serial,      -- 标签所在图像id
    x_position integer,     -- 标签位置横坐标（TODO：应该使用double）
    y_position integer,     -- 标签位置纵坐标（TODO：应该使用double）
    text       varchar(16), -- 标签上文字
    create_ts  timestamp,
    update_ts  timestamp,
    constraint tag_pk primary key (tag_id),
    constraint tag_fk1 foreign key (img_id) references image (img_id)
);

-- 话题表：每篇文章可以属于多个话题
create table topic
(
    topic_id   serial,      -- 主键
    topic_name varchar(16), -- 话题名称
    create_ts  timestamp,
    update_ts  timestamp,
    constraint topic_pk primary key (topic_id)
);

-- 文章与话题关系表（N : N）
create table article_topic_relation
(
    id         bigserial, -- 行id，主键
    article_id integer,   -- 文章id
    topic_id   integer,   -- 文章所属话题id
    create_ts  timestamp,
    update_ts  timestamp,
    constraint article_topic_relation_pk primary key (id),
    constraint article_topic_relation_fk1 foreign key (article_id) references article (article_id),
    constraint article_topic_relation_fk2 foreign key (topic_id) references topic (topic_id)
);

-- 评论表
create table comment
(
    comment_id serial,       -- 主键
    text_body  varchar(512), -- 评论正文
    likes      integer,      -- 评论获得的点赞数
    article_id integer,      -- 评论所属的文章id
    author_id  integer,      -- 评论者id
    create_ts  timestamp,
    update_ts  timestamp,
    constraint comment_pk primary key (comment_id),
    constraint comment_fk1 foreign key (article_id) references article (article_id),
    constraint comment_fk2 foreign key (author_id) references hp_user (user_id)
);

-- 评论回复表：评论可被回复，评论的回复不直接属于文章，而是属于所回复的评论。故单独作为一个表
create table comment_reply
(
    reply_id   serial,       -- 主键
    article_id integer,      -- 所回复的评论所属的文章id
    comment_id integer,      -- 所回复的评论的id
    text_body  varchar(512), -- 回复正文
    likes      integer,      -- 回复获得的点赞数
    replier_id integer,      -- 回复者id
    create_ts  timestamp,
    update_ts  timestamp,
    constraint comment_reply_pk primary key (replier_id),
    constraint comment_reply_fk1 foreign key (article_id) references article (article_id),
    constraint comment_reply_fk2 foreign key (replier_id) references hp_user (user_id),
    constraint comment_reply_fk3 foreign key (comment_id) references comment (comment_id)
);

-- 用户与分类关系表（N : N）
-- 一个用户可以关注多个分类，一个分类可以被多个用户关注
create table user_category_relation
(
    id          bigserial, -- 行id，主键
    user_id     integer,
    category_id integer,
    create_ts   timestamp,
    update_ts   timestamp,
    constraint user_category_relation_pk primary key (id),
    constraint user_category_relation_fk1 foreign key (user_id) references hp_user (user_id),
    constraint user_category_relation_fk2 foreign key (category_id) references category (category_id)
);

-- 用户（关注者）与用户（被关注者）关系表（N : N）
-- 一个用户可以关注多个用户，一个用户可以被多个用户关注
create table user_following_relation
(
    id                bigserial, -- 行id，主键
    user_id           integer,   -- 被关注用户id
    following_user_id integer,   -- 关注者id
    group_id          integer,   -- 被关注用户所在分组的id
    create_ts         timestamp,
    update_ts         timestamp,
    constraint user_following_relation_pk primary key (id),
    constraint user_following_relation_fk1 foreign key (user_id) references hp_user (user_id),
    constraint user_following_relation_fk2 foreign key (following_user_id) references hp_user (user_id),
    constraint user_following_relation_fk3 foreign key (group_id) references user_group (group_id)
);

-- 专辑与文章关系表（N : N）
-- 一个专辑可以包含多篇文章，一篇文章可以被多个专辑收录
create table album_article_relation
(
    id         bigserial, -- 行id，主键
    album_id   integer,   -- 专辑id
    article_id integer,   -- 文章id
    create_ts  timestamp,
    update_ts  timestamp,
    constraint album_article_relation_pk primary key (id),
    constraint album_article_relation_fk1 foreign key (album_id) references album (album_id),
    constraint album_article_relation_fk2 foreign key (article_id) references article (article_id)
);

-- 用户和专辑关系表
-- 一个用户可以关注多个专辑， 一个专辑可被多个用户关注
create table user_album_relation
(
    id          bigserial, -- 行id，主键
    follower_id integer,   -- 关注者id
    album_id    integer,   -- 所关注专辑的id
    create_ts   timestamp,
    update_ts   timestamp,
    constraint user_album_relation_pk primary key (id),
    constraint user_album_relation_fk1 foreign key (album_id) references album (album_id),
    constraint user_album_relation_fk2 foreign key (follower_id) references article (article_id)
);

-- 文章与点赞用户关系表（N : N）
-- 一个文章可以被多个用户点赞，一个用户可以点赞多篇文章
-- 点赞者的粉丝数用于决定在显示"用户1，用户2等11人已点赞"时使用哪些用户（粉丝数最多的两个人）
create table article_like_relation
(
    id                               bigserial, -- 行id，主键
    liker_id                         integer,   -- 点赞者id
    article_id                       integer,   -- 文章id
    liker_follower_count_approximate integer,   -- 点赞者的粉丝数（近似值）
    create_ts                        timestamp,
    update_ts                        timestamp,
    constraint article_like_relation_pk primary key (id),
    constraint article_like_relation_fk1 foreign key (liker_id) references hp_user (user_id),
    constraint article_like_relation_fk2 foreign key (article_id) references article (article_id)
);

-- 文章与收藏者关系表
-- 一个文章可以被多个用户收藏，一个用户可以收藏多篇文章
-- 收藏者的粉丝数用于决定在显示"用户1，用户2等11人已收藏"时使用哪些用户（粉丝数最多的两个人）
create table article_collect_relation
(
    id                                   bigserial, -- 行id，主键
    collector_id                         integer,   -- 收藏者id
    article_id                           integer,   -- 文章id
    collector_follower_count_approximate integer,   -- 收藏者粉丝数 （近似值）
    create_ts                            timestamp,
    update_ts                            timestamp,
    constraint article_collect_relation_pk primary key (id),
    constraint article_collect_relation_fk1 foreign key (collector_id) references hp_user (user_id),
    constraint article_collect_relation_fk2 foreign key (article_id) references article (article_id)
);