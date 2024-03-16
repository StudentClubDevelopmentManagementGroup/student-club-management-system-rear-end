package team.project.module.lin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.lin.service.TmplService;

@Tag(name="TmplController")
@RestController
public class TmplController {
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
    @Operation(summary="查询 tmp_test 表中的所有记录")
    @GetMapping("/tmpl/list")
    Object list() {
        //装填数据
        ///


        //传回前端
        return new Response<>(ServiceStatus.SUCCESS)
            .statusText("查询成功")
            .data(service.list());
    }

}
