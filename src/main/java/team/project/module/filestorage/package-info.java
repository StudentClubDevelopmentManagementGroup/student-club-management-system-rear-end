/**
 * <h3> 介绍
 * <p>  本模块负责处理文件存取，提供将文件存储到“本地文件系统”和“云存储空间”两类接口
 * <p> （在哪里运行这个后端，哪里就是“本地”）
 * <p>  本模块绑定 file-storage-config.yml 配置文件
 * <p>
 * <p>  目前用阿里云 OSS 作云存储
 * <p>  <a href="https://help.aliyun.com/zh/oss/developer-reference/java/">
 *      如何快速使用OSS Java SDK完成常见操作
 * <p>
 */
package team.project.module.filestorage;
