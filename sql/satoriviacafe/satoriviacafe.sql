-- ----------------------------
-- 0、咖啡产品访问跟踪日志表
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
-- 1、产品线
-- ----------------------------
DROP TABLE IF EXISTS cafe_product_line;
CREATE TABLE cafe_product_line
(
    line_id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '产品线id',
    line_code          VARCHAR(32)  NOT NULL COMMENT 'URL产品类型：beans/drip，对应前端productsid',
    series_code        VARCHAR(64)  NOT NULL COMMENT 'URL系列编码：asteroids/drip，对应前端series',
    line_name          VARCHAR(100) NOT NULL COMMENT '产品线名称',
    line_sub_title     VARCHAR(255)  DEFAULT '' COMMENT '产品线副标题',
    line_description   VARCHAR(1000) DEFAULT '' COMMENT '产品线说明',
    cover_image        VARCHAR(500) NOT NULL COMMENT '产品线桌面端封面',
    mobile_cover_image VARCHAR(500)  DEFAULT '' COMMENT '产品线手机端封面',
    seo_title          VARCHAR(255)  DEFAULT '' COMMENT 'SEO标题',
    seo_description    VARCHAR(500)  DEFAULT '' COMMENT 'SEO描述',
    line_sort          INT           DEFAULT 0 COMMENT '显示顺序',
    line_status        CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by          VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time        DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by          VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time        DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at          DATETIME      DEFAULT NULL COMMENT '删除时间',
    UNIQUE KEY uk_line_route (line_code, series_code),
    KEY idx_line_status_sort (line_status, line_sort)
) ENGINE = InnoDB COMMENT = '咖啡产品线表';

-- ----------------------------
-- 2、商品主表
-- ----------------------------
DROP TABLE IF EXISTS cafe_product;
CREATE TABLE cafe_product
(
    product_id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品id',
    line_id             BIGINT       NOT NULL COMMENT '产品线id，关联cafe_product_line.line_id',
    product_slug        VARCHAR(128) NOT NULL COMMENT 'URL商品编码，对应前端id',
    product_name        VARCHAR(255) NOT NULL COMMENT '商品完整名称',
    product_short_name  VARCHAR(100)  DEFAULT '' COMMENT '商品短名称',
    product_label       VARCHAR(64)   DEFAULT '精品咖啡' COMMENT '商品标签',
    product_summary     VARCHAR(1000) DEFAULT '' COMMENT '商品摘要',
    flavor              VARCHAR(500) NOT NULL COMMENT '风味特点',
    origin              VARCHAR(255) NOT NULL COMMENT '产地/庄园',
    altitude            VARCHAR(100) NOT NULL COMMENT '海拔高度',
    variety             VARCHAR(255) NOT NULL COMMENT '咖啡品种',
    treatment           VARCHAR(500) NOT NULL COMMENT '处理方式',
    roast_degree        VARCHAR(100)  DEFAULT '' COMMENT '烘焙度',
    brewing_method      VARCHAR(1000) DEFAULT '' COMMENT '推荐冲煮方法概述',
    dosage              VARCHAR(100)  DEFAULT '' COMMENT '建议粉量/用量',
    extraction_time     VARCHAR(100)  DEFAULT '' COMMENT '萃取时间',
    water_temp          VARCHAR(100)  DEFAULT '' COMMENT '建议水温',
    powder_water_ratio  VARCHAR(100)  DEFAULT '' COMMENT '粉水比',
    specification       VARCHAR(255)  DEFAULT '' COMMENT '规格概述',
    package_desc        VARCHAR(500)  DEFAULT '' COMMENT '包装说明',
    additives           VARCHAR(500)  DEFAULT '' COMMENT '添加物说明',
    product_description LONGTEXT COMMENT '商品详情正文',
    product_features    LONGTEXT COMMENT '产品特色，建议存JSON数组或HTML',
    primary_image       VARCHAR(500) NOT NULL COMMENT '商品主图/产品线列表封面',
    radar_image         VARCHAR(500)  DEFAULT '' COMMENT '风味雷达图',
    purchase_url        TEXT COMMENT '默认外部购买链接',
    seo_title           VARCHAR(255)  DEFAULT '' COMMENT 'SEO标题',
    seo_description     VARCHAR(500)  DEFAULT '' COMMENT 'SEO描述',
    og_image            VARCHAR(500)  DEFAULT '' COMMENT '社交分享图片',
    is_featured         CHAR(1)       DEFAULT '0' COMMENT '是否推荐（0否 1是）',
    product_sort        INT           DEFAULT 0 COMMENT '产品线内排序',
    product_status      CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    publish_time        DATETIME      DEFAULT NULL COMMENT '发布时间',
    create_by           VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time         DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time         DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at           DATETIME      DEFAULT NULL COMMENT '删除时间',
    UNIQUE KEY uk_product_slug (line_id, product_slug),
    KEY idx_product_line_status_sort (line_id, product_status, product_sort),
    KEY idx_product_featured (is_featured, product_status)
) ENGINE = InnoDB COMMENT = '咖啡商品主表';

-- ----------------------------
-- 3、商品规格
-- ----------------------------
DROP TABLE IF EXISTS cafe_product_variant;
CREATE TABLE cafe_product_variant
(
    variant_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品规格id',
    product_id     BIGINT       NOT NULL COMMENT '商品id',
    variant_name   VARCHAR(100) NOT NULL COMMENT '规格名称，例如227g、454g',
    sku_code       VARCHAR(100)   DEFAULT '' COMMENT '外部或内部SKU编码',
    net_content    VARCHAR(100)   DEFAULT '' COMMENT '净含量',
    sale_price     DECIMAL(10, 2) DEFAULT NULL COMMENT '销售价格',
    market_price   DECIMAL(10, 2) DEFAULT NULL COMMENT '划线价',
    stock_status   CHAR(1)        DEFAULT '0' COMMENT '库存状态（0有货 1缺货 2预售）',
    purchase_url   TEXT COMMENT '该规格的外部购买链接',
    variant_sort   INT            DEFAULT 0 COMMENT '显示顺序',
    variant_status CHAR(1)        DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by      VARCHAR(64)    DEFAULT '' COMMENT '创建者',
    create_time    DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by      VARCHAR(64)    DEFAULT '' COMMENT '更新者',
    update_time    DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at      DATETIME       DEFAULT NULL COMMENT '删除时间',
    UNIQUE KEY uk_product_variant (product_id, variant_name),
    KEY idx_variant_product (product_id, variant_status, variant_sort)
) ENGINE = InnoDB COMMENT = '咖啡商品规格表';

-- ----------------------------
-- 4、商品图库
-- ----------------------------
DROP TABLE IF EXISTS cafe_product_image;
CREATE TABLE cafe_product_image
(
    image_id      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品图片id',
    product_id    BIGINT       NOT NULL COMMENT '商品id',
    product_image VARCHAR(500) NOT NULL COMMENT '图片地址',
    image_alt     VARCHAR(255) DEFAULT '' COMMENT '图片替代文本',
    image_type    VARCHAR(20)  DEFAULT 'gallery' COMMENT '图片类型：gallery/detail/og',
    image_sort    INT          DEFAULT 0 COMMENT '显示顺序',
    image_status  CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by     VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by     VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at     DATETIME     DEFAULT NULL COMMENT '删除时间',
    KEY idx_image_product (product_id, image_type, image_status, image_sort)
) ENGINE = InnoDB COMMENT = '咖啡商品图库表';

-- ----------------------------
-- 5、商品冲煮方案
-- ----------------------------
DROP TABLE IF EXISTS cafe_product_brew;
CREATE TABLE cafe_product_brew
(
    brew_id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '冲煮方案id',
    product_id         BIGINT       NOT NULL COMMENT '商品id',
    brew_name          VARCHAR(100) NOT NULL COMMENT '方案名称，例如推荐参数、V60',
    brew_method        VARCHAR(255) DEFAULT '' COMMENT '冲煮方式/器具',
    powder_quantity    VARCHAR(100) DEFAULT '' COMMENT '粉量',
    water_volume       VARCHAR(100) DEFAULT '' COMMENT '水量',
    water_temp         VARCHAR(100) DEFAULT '' COMMENT '水温',
    grind_degree       VARCHAR(100) DEFAULT '' COMMENT '研磨度',
    powder_water_ratio VARCHAR(100) DEFAULT '' COMMENT '粉水比',
    extraction_time    VARCHAR(100) DEFAULT '' COMMENT '萃取时间',
    target_time        VARCHAR(100) DEFAULT '' COMMENT '目标总时间',
    preparations       TEXT COMMENT '准备工作',
    brew_steps         TEXT COMMENT '冲煮步骤',
    brew_sort          INT          DEFAULT 0 COMMENT '显示顺序',
    brew_status        CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by          VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by          VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at          DATETIME     DEFAULT NULL COMMENT '删除时间',
    KEY idx_brew_product (product_id, brew_status, brew_sort)
) ENGINE = InnoDB COMMENT = '咖啡商品冲煮方案表';


-- ----------------------------
-- 6、品牌故事表
-- ----------------------------
drop table if exists cafe_brand_story;
create table cafe_brand_story
(
    story_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '品牌故事id',
    story_title  VARCHAR(255) NOT NULL COMMENT '品牌故事标题',
    story_image  VARCHAR(255) default '' comment '品牌故事图片',
    story_text   TEXT COMMENT '品牌故事详情',
    story_status CHAR(1)      default '0' comment '品牌故事状态（0正常 1停用）',
    create_by    VARCHAR(64)  default '' comment '创建者',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at    DATETIME     DEFAULT NULL COMMENT '删除时间'
) engine = innodb comment = '品牌故事表';

-- ----------------------------
-- 7、轮播图
-- ----------------------------
DROP TABLE IF EXISTS cafe_banner;
CREATE TABLE cafe_banner
(
    banner_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '轮播图id',
    banner_title  VARCHAR(255) NOT NULL COMMENT '轮播图标题',
    banner_image VARCHAR(255) NOT NULL COMMENT '轮播图图片',
    banner_link   VARCHAR(255) NOT NULL COMMENT '轮播图链接',
    banner_desc   VARCHAR(255) DEFAULT '' COMMENT '轮播图描述',
    banner_status CHAR(1)      DEFAULT '0' COMMENT '轮播图状态（0正常 1停用）',
    create_by     VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by     VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at    DATETIME DEFAULT NULL COMMENT '删除时间'
) engine = innodb comment = '轮播图表';

-- ----------------------------
-- 8、豆子笔记
-- ----------------------------
DROP TABLE IF EXISTS cafe_beans_note;
CREATE TABLE cafe_beans_note
(
    note_id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '豆子笔记id',
    note_title     VARCHAR(255) NOT NULL COMMENT '豆子笔记标题',
    note_sub_title VARCHAR(255) NOT NULL COMMENT '豆子笔记副标题',
    note_content   TEXT         NOT NULL COMMENT '豆子笔记内容',
    note_image VARCHAR(255) NOT NULL COMMENT '豆子笔记图片',
    brew_time      VARCHAR(255) NOT NULL COMMENT '冲泡时间',
    poder_quantity VARCHAR(255) NOT NULL COMMENT '粉量',
    water_volume   VARCHAR(255) NOT NULL COMMENT '水量',
    water_temp     VARCHAR(255) NOT NULL COMMENT '水温',
    grind_degree   VARCHAR(255) NOT NULL COMMENT '研磨度',
    target_time    VARCHAR(255) NOT NULL COMMENT '目标总时间',
    preparations   TEXT         NOT NULL COMMENT '准备工作',
    brew_steps     TEXT         NOT NULL COMMENT '冲泡步骤',
    note_status    CHAR(1)     DEFAULT '0' COMMENT '豆子笔记状态（0正常 1停用）',
    create_by      VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by      VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time    DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at      DATETIME    DEFAULT NULL COMMENT '删除时间'
) engine = innodb comment = '豆子笔记表';


