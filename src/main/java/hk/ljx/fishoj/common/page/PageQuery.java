package hk.ljx.fishoj.common.page;

import lombok.Data;

/**
 * 分页查询基类, 业务查询 dto 直接继承即可
 */
@Data
public class PageQuery {

    private long page = 1;

    private long size = 10;
}