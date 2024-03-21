package team.project.module.useraccount.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.useraccount.internal.model.request.RegisterReq;
import team.project.module.useraccount.internal.service.RegisterService;

@Tag(name="用户身份验证、账号管理")
@RestController
public class RegisterController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RegisterService registerService;

    @Operation(summary="注册账号")
    @PostMapping("/auth/register")
    Object register(@Valid @RequestBody RegisterReq req) {
        registerService.register(req);
        return new Response<>(ServiceStatus.CREATED).statusText("注册成功");
    }

}
