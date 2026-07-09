package hk.ljx.fishoj.common.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页查询基类, 业务查询 dto 直接继承即可
 */
@Data
public class PageQuery {

    @Min(value = 1, message = "页码最小为1")
    private long page = 1;

    @Min(value = 1, message = "每页最少1条")
    @Max(value = 100, message = "每页最多100条")
    private long size = 10;
}