/**
 * <h3>介绍</h3>
 * <p>本模块负责处理用户身份验证、账号管理</p>
 * <p>包括：用户注册、注销、登录、登出</p>
 * <p></p>
 *
 * <h3>关于注册</h3>
 * <p>先不考虑验证学号/工号的正确性，因为学校未提供合适的接口，我们也做不到验证</p>
 * <p>如果学校提供了接口，我们可以通过学号/工号判断注册者的身份（是学生或是教师）</p>
 * <p>现在，我们无法避免下述情况的恶意注册：</p>
 * <p> - 用别人的（或是不存在的）学号/工号注册</p>
 * <p> - 学生（教师）注册了用户，但选择用户的身份是教师（学生）</p>
 * <p></p>
 *
 * <h3>关于注销</h3>
 * <p>我们只是将用户账号逻辑删除，数据库中保留有用户信息（包括学号/工号）</p>
 * <p>由于学号/工号的“唯一键”约束，所以用户无法以相同学号/工号再注册账号</p>
 * <p>我们期望的办法是：用户与管理员联系，撤销逻辑删除</p>
 * <p>但是，正如上方所说，我们无法保证用户拿别人的（或是不存在的）学号/工号注册新账号</p>
 * <p></p>
 */
package team.project.module.user;
