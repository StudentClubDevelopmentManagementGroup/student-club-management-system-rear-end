/**
 * <h1> 介绍
 * <p>  本模块负责处理文件存取，提供“本地存储”和“云存储”两类接口
 * <p>  本模块绑定 file-storage-config.yml 配置文件
 * <p>
 * <h2> 本地存储
 * <p>  在哪里运行这个后端，哪里就是“本地”
 * <p>  开发阶段，后端运行在各编码人员的 Windows 电脑上，那么本地文件系统就是本地硬盘
 * <p>  部署后，后端可能运行在 linux 服务器上，那么本地文件系统就是服务器的硬盘
 * <p>
 * <h2> 云存储
 * <p>  目前用阿里云 OSS 作云存储
 * <a href="https://help.aliyun.com/zh/oss/developer-reference/java/">
 *      如何快速使用OSS Java SDK完成常见操作
 * </a>
 * <p>
 * <h1> 路径规则
 * <p>  本模块中，所有涉及“文件路径”的填写（配置文件、接口入参），均遵守如下规则：
 * <ul>
 * <li> 使用 / 作为分隔符
 * <li> 不要出现连续的 /
 * <li> 如果是相对路径，以 / 开头
 * <li> 不以 / 或 . 结尾
 * <li> 不要使用 /.. 和 /. 跨文件夹
 * <li> 不含非法字符： * : ? " ' < > |
 * </ul>
 * <p>
 */
package team.project.module.util.filestorage;