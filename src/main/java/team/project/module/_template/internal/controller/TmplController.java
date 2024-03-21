package team.project.module._template.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module._template.internal.service.TmplService;

@Tag(name="【测试】模板示例")
@RestController
public class TmplController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TmplService service; /* 示例 */

    /* 示例 */
    @Operation(summary="返回 hello world! 文本")
    @GetMapping("/tmpl/hello-world")
    Object helloWorld() {
        String str = "hello world!";

        return new Response<>(ServiceStatus.SUCCESS) /* 返回 service 状态码 */
            .statusText("OK") /* 可选，携带状态信息（可以用于前端的报错展示，如果前端懒的话） */
            .data(str);       /* 可选，携带数据 */
    }

    /* 示例 */
    @Operation(summary="使用 mybatis 查询 tmp_test 表中的所有记录")
    @GetMapping("/tmpl/list")
    Object list() {
        return new Response<>(ServiceStatus.SUCCESS)
            .statusText("查询成功")
            .data(service.list());
    }

    /* 示例 */
    @Operation(summary="使用 mybatis-plus 查询 tmp_test 表中的所有记录")
    @GetMapping("/tmpl/list_mp")
    Object list2() {
        return new Response<>(ServiceStatus.SUCCESS)
            .statusText("查询成功")
            .data(service.list_mp());
    }

}
