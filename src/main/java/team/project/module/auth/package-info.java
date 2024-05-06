/**
 * <h1> 介绍
 * <p>  本模块负责处理用户角色的校验，提供用于角色鉴权的方法
 * <p>  同时，本模块整合了 Sa-Token 框架，使得项目可以使用框架提供的注解和方法进行鉴权
 * <p><a href="https://sa-token.cc/doc.html#/">
 *      Sa-Token 说明文档 </a>
 * <br> F12 在控制台执行 <u> localStorage.isStarRepo = new Date().getTime() </u> 后刷新页面，可绕过弹窗
 * <p>
 * <h1> 角色、权限划分
 * <p>  本项目划分有如下角色：
 * <ul>
 * <li> 学生
 * <li> 教师
 * <li> 社团成员
 * <li> 社团负责人
 * <li> 超级管理员
 * </ul>
 * <p>  一个用户拥有多重角色，每个角色对应一组权限：
 * <ul>
 * <li> 学生、教师：暂无特殊权限，仅作身份标记
 * <li> 社团成员：在其所在的社团内部拥有部分权限
 * <li> 社团负责人：仅对其负责的社团拥有所有权限
 * <li> 超级管理员：拥有<b>所有</b>权限（即使不是社团负责人，也能操控社团内部所有事物）
 * </ul>
 * <h1> 对谁鉴权
 * <p>  对请求的发起者进行鉴权，鉴定其“是否拥有权力”来执行该操作
 * <p>  例如：<u>社团负责人给社团成员分配座位</u>
 * <br> 则验证发起请求的用户是否拥有“社团负责人”角色，而且必须是“这个社团的负责人”，确保其有“分配座位”的权力。
 *      业务逻辑还可能会要求：只许分给“这个社团的成员”，不能给其他人。
 *      “社团成员”不是执行者，其身份的校验不属于鉴权，更多是属于保证业务逻辑正确和保证数据正确
 * <p>
 * <h1> 使用 Sa-Token 提供的注解进行鉴权
 * <p>  Sa-Token 提供注解鉴权和方法鉴权两种方式，推荐使用注解鉴权
 * <p>  使用注解使得将鉴权与业务代码更分离，而且代码写起来更简单，只需在 controller 的方法上加上鉴权注解即可
 * <p>  框架会识别出携带有鉴权注解的方法，收到 http 请求时，会先在 interceptor 层校验身份，
 *      校验通过后请求才会进入 controller 层，所以用注解比调方法执行起来更高效
 * <p>
 * <h2> 登录校验
 * <p>  使用 <b> @SaCheckLogin </b> 修饰的方法，只有登录之后才能进入
 * <p>  已做了配置，用户登录后框架保存 userId，之后都是通过 userId 进行其他校验
 * <p>
 * <h2> 获取当前登录用户的 userId
 * <p>  <b> String userId = (String)( StpUtil.getSession().getLoginId() ); </b>
 * <p>
 * <h2> 角色校验
 * <p>  使用 <b> @SaCheckRole </b> 修饰的方法，必须具有指定角色（且已登录）才能进入
 * <p>  例如：
 * <br> 用 @SaCheckRole( AuthRole.CLUB_MANAGER ) ，可以校验用户是否为“社团负责人”
 * <br> （这只是初步校验，只用到 userId 而没用到 clubId，所以，如果校验通过，只能说明该用户是某社团的负责人，
 *      并不知道到底是哪个社团的负责人，故之后还需进一步校验）
 * <p>  注意：
 * <ol>
 * <li> 角色码请使用枚举常量：{@link team.project.module.auth.export.model.enums.AuthRole AuthRole}
 * <li> @SaCheckRole 自带登录校验，不需要多写 @SaCheckLogin
 * <li> 如果用户是“超级管理员”，则可以通过所有的 @SaCheckRole( XXX ) 的校验
 * <br> 不需要多写 @SaCheckRole( AuthRole.SUPER_ADMIN )
 * <br> 这是考虑到“超级管理员”角色的特殊性，针对鉴权做的特例。详见拦截器配置中
 *      {@link team.project.module.auth.internal.interceptor.InterceptorConfig#getRoleList getRoleList()}
 *      的实现逻辑
 * <br> 数据库中保存的角色码是分开的，“超级管理员”和其他角色互不干涉
 * </ol>
 * <h1> 进一步鉴权
 * <p>  Sa-Token 框架可以完成一部分鉴权工作，除此之外本模块还提供有用于进一步角色鉴权的方法，
 * <br> 这些进一步的鉴权方法，实际上是整合了各模块所提供的，关于判断用户身份、角色的方法
 * <br> 详见：{@link team.project.module.auth.export.service.AuthServiceI}
 * <p>
 */
package team.project.module.auth;
