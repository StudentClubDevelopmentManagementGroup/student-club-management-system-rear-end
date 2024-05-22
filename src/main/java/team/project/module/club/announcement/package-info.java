/**
 * <h1> 介绍
 * <p>  本模块负责管理社团公告
 * <p>
 * <h1> 命名约定
 * <p>  单词 announcement（公告）太长，又在本模块中频繁出现，故使用缩写。但该词没有统一的缩写标准，
 *      有几种可能的缩写形式： ann、anno、anmnt、announc、anncmnt
 * <p>  本模块中，定义 <b> ann </b> 为 announcement（公告）的缩写
 * <p>
 * <h1> 权限说明
 * <p>  约定：如果最低执行权限是“社团负责人”，则默认“超级管理员”也可以执行，除非特别说明
 * <p>  详见 {@link team.project.module.auth 鉴权模块的package-info.java} 中对角色、权限划分的介绍
 * <ul>
 * <li> 保存草稿：
 * <p>  公告可以直接发布，也可以暂存草稿
 * <br> 需要社团负责人权限才能保存草稿
 * <br>
 * <li> 查看草稿、更新草稿、删除草稿：
 * <p>  草稿箱里只能查看自己的草稿，不能查看别人的草稿（同社团不同负责人之间的草稿箱不互通）
 * <br> 只能作者本人能更新、删除自己的草稿
 * <br>
 * <li> 发布公告：
 * <p>  需要社团负责人权限才能发公告
 * <br>
 * <li> 更新公告：
 * <p>  不能更新公告，每篇公告都是只读的，不能更改
 * <br> 如果要修改自己发的公告，可以将公告移入草稿箱，编辑后发布新公告，再自己手动删除旧公告
 * <br> 但不能将别人发的公告移入自己的草稿箱
 * <br>
 * <li> 删除公告：
 * <p>  若是删自己发的公告，需要社团负责人权限（如果作者在发布时有权限，删除时没有，那也不能删除公告）
 * <br> 若是删别人发的公告，同社团内教师负责人能删学生发的公告，超级管理员能删教师发的公告
 * <br>
 * <li> 搜索公告、查看公告：
 * <p>  暂时不设权限
 * </ul>
 * <p>
 */
package team.project.module.club.announcement;
