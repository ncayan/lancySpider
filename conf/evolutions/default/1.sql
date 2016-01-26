# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table COMPETITOR (
  id                        bigint not null,
  brand_name                varchar(255),
  start_url                 varchar(255),
  plat_name                 varchar(255),
  constraint pk_COMPETITOR primary key (id))
;

create table COMPETITOR_ITEMS (
  id                        bigint not null,
  brand_name                varchar(255),
  plat_name                 varchar(255),
  item_name                 varchar(255),
  item_url                  varchar(255),
  img_url                   varchar(255),
  drop_price                varchar(255),
  item_price                varchar(255),
  total_volume              varchar(255),
  month_volume              varchar(255),
  rates                     varchar(255),
  season                    varchar(255),
  product_id                varchar(255),
  create_time               timestamp,
  item_des                  varchar(255),
  constraint pk_COMPETITOR_ITEMS primary key (id))
;

create table plat (
  id                        bigint not null,
  plat_name                 varchar(255),
  area_regular              varchar(255),
  url_regular               varchar(255),
  img_regular               varchar(255),
  name_regular              varchar(255),
  price_regular             varchar(255),
  volume_regular            varchar(255),
  rates_regular             varchar(255),
  drop_price_class          varchar(255),
  month_volume_class        varchar(255),
  rates_class               varchar(255),
  attr_list_class           varchar(255),
  constraint pk_plat primary key (id))
;

create sequence COMPETITOR_seq;

create sequence COMPETITOR_ITEMS_seq;

create sequence plat_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists COMPETITOR;

drop table if exists COMPETITOR_ITEMS;

drop table if exists plat;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists COMPETITOR_seq;

drop sequence if exists COMPETITOR_ITEMS_seq;

drop sequence if exists plat_seq;

