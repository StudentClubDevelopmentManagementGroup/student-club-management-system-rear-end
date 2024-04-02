package team.project.module.user.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.internal.model.request.UserIdAndPasswordReq;
import team.project.module.user.internal.service.RegisterService;

@Tag(name="注册")
@RestController
public class RegisterController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RegisterService service;

    @Operation(summary="注册账号")
    @PostMapping("/user/register")
    Object register(@Valid @RequestBody RegisterReq req) {
        service.register(req);
        return new Response<>(ServiceStatus.CREATED).statusText("注册成功");
    }

    @Operation(summary="注销账号")
    @PostMapping("/user/unregister")
    Object unregister(@Valid @RequestBody UserIdAndPasswordReq req) {
        service.unregister(req.getUserId(), req.getPassword());
        return new Response<>(ServiceStatus.SUCCESS).statusText("销号成功");
    }
}
