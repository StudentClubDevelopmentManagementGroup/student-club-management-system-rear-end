# 文件存储模块的设计细节

> 安装 [mermaid](https://mermaid.js.org/) 插件后，标有 mermaid 的代码块将被渲染成 UML 图

## 上传文件

``` mermaid
sequenceDiagram
    box 其他模块
        participant x_service as Service
    end
    box 文件存储模块
        participant f_service as 文件存储 Service
        participant l_DAO as 本地文件存储 DAO
        participant c_DAO as 阿里云 OSS DAO
    end

    x_service ->> f_service : 给定文件、目标存储路径、存储类型，上传文件
    f_service ->> f_service : 依据目标路径和存储类型生成 file_id
    alt  如果存储类型是 LOCAL
        f_service ->> l_DAO : 保存文件
    else 如果存储类型是 CLOUD
        f_service ->> c_DAO : 上传文件
    end
    f_service -->> x_service : file_id
```

## 获取访问文件的 url

``` mermaid
sequenceDiagram
    box 其他模块
        participant x_service as Service
    end
    box 文件存储模块
        participant f_service as 文件存储 Service
        participant l_DAO as 本地文件存储 DAO
        participant c_DAO as 阿里云 OSS DAO
    end

    x_service ->> f_service : 给定 file_id，获取文件
    f_service ->> f_service : 依据 file_id 辨别存储类型，提取出文件路径
    alt  如果存储类型是 LOCAL
        f_service ->> l_DAO : 文件路径
        l_DAO -->> f_service : 访问文件的 url
    else 如果存储类型是 CLOUD
        f_service ->> c_DAO : 文件路径
        c_DAO -->> f_service : 访问文件的 url
    end
    f_service -->> x_service : 访问文件的 url
```
