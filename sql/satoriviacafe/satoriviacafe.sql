-- ----------------------------
-- 1、产品表
-- ----------------------------
drop table if exists cafe_production;
create table cafe_production
(
    prod_id   bigint(20)  not null auto_increment comment '产品id',
    prod_name   varchar(30)    default '' comment '产品名称',
    prod_desc   varchar(255)   default '' comment '产品描述',
    prod_img    varchar(255)   default '' comment '产品图片',
    prod_code varchar(64) not null comment '商品编码',
    prod_price  decimal(10, 2) default 0.00 comment '产品价格',
    prod_text   text comment '产品详情',
    order_num   int(4)         default 0 comment '显示顺序',
    prod_status char(1)        default '0' comment '产品状态（0正常 1停用）',
    create_by   varchar(64)    default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)    default '' comment '更新者',
    update_time datetime comment '更新时间',
    delete_at   datetime       default NULL comment '删除时间',
    primary key (prod_id),
    unique key (prod_code)
) engine = innodb
  auto_increment = 200 comment = '产品表';

-- ----------------------------
-- 2、品牌故事表
-- ----------------------------
drop table if exists cafe_brand_story;
create table cafe_brand_story
(
    story_id     bigint(20) not null auto_increment comment '品牌故事id',
    story_img    varchar(255) default '' comment '品牌故事图片',
    story_text   text comment '品牌故事详情',
    story_status char(1)      default '0' comment '品牌故事状态（0正常 1停用）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    delete_at    datetime     default NULL comment '删除时间',
    primary key (story_id)
) engine = innodb
  auto_increment = 200 comment = '品牌故事表';
