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
) engine = innodb comment = '产品表';

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
) engine = innodb comment = '品牌故事表';

-- ----------------------------
-- 3、咖啡产品访问跟踪日志表
-- ----------------------------
DROP TABLE IF EXISTS cafe_track_log;
CREATE TABLE cafe_track_log
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    prod_id      VARCHAR(64)  NOT NULL COMMENT '产品ID',
    prod_code    VARCHAR(64)  NOT NULL COMMENT '商品编码',
    prod_name    VARCHAR(255) NOT NULL COMMENT '产品名称 (如: 埃塞瑰夏精品咖啡豆)',
    event_name   VARCHAR(100) NOT NULL COMMENT '事件类型 (如: page_view)',
    ip           VARCHAR(45) COMMENT '访问者IP地址 (支持IPv6)',
    location     VARCHAR(255) COMMENT '访问者地理位置',
    user_agent   TEXT COMMENT '原始浏览器User-Agent字符串',
    device_type  VARCHAR(50) COMMENT '设备类型 (如: Mobile, Desktop, Tablet)',
    browser      VARCHAR(50) COMMENT '浏览器名称 (如: Chrome, WeChat, Safari)',
    os           VARCHAR(50) COMMENT '操作系统 (如: iOS, Android, Windows)',
    page_url     TEXT COMMENT '当前页面URL',
    referrer_url TEXT COMMENT '来源页面URL',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    -- 为常用查询字段建立索引
    INDEX `idx_prod_code` (`prod_code`),
    INDEX `idx_event_name` (`event_name`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB COMMENT ='咖啡产品访问跟踪日志表';

-- ----------------------------
-- 4、轮播图
-- ----------------------------
drop table if exists cafe_banner;
create table cafe_banner
(
    banner_id     bigint(20)   not null auto_increment comment '轮播图id',
    banner_title  varchar(255) not null comment '轮播图标题',
    banner_img    varchar(255) not null comment '轮播图图片',
    banner_link   varchar(255) not null comment '轮播图链接',
    banner_desc   varchar(255) default '' comment '轮播图描述',
    banner_status char(1)      default '0' comment '轮播图状态（0正常 1停用）',
    create_by     varchar(64)  default '' comment '创建者',
    create_time   datetime comment '创建时间',
    update_by     varchar(64)  default '' comment '更新者',
    update_time   datetime comment '更新时间',
    delete_at     datetime     default NULL comment '删除时间',
    primary key (banner_id)
) engine = innodb comment = '轮播图表';


