
# 公告模块的设计细节

> 安装 [mermaid](https://mermaid.js.org/) 插件后，标有 mermaid 的代码块将被渲染成 UML 图

公告内容以富文本的形式存储，将HTML格式的字符串保存在文本文件中，而文本文件保存AliyunOSS中，
公告所携带的图片也存储在AliyunOSS中。

展示一篇公告内容的流程如下（省去了dao层，但不要紧）：

``` mermaid
sequenceDiagram
    box web前端
        participant web as web浏览器
    end
    box 公告模块
        participant controller as controller
        participant service as service
        participant db as MySQL数据库
    end
    box 文件存储模块
        participant f_service as service
        participant storage as AliyunOSS
    end

    web ->> controller : 给定公告id，获取详情
    controller ->> service : 给定公告id，获取详情
    service ->> db : 给定公告id，查询基本信息
    db -->> service : 返回基本信息
    service ->> f_service : 给定file_id，获取公告内容
    f_service ->> storage : 获取文件内容
    storage -->> f_service : 返回文件内容
    f_service -->> service : 返回公告内容
    service -->> controller : 返回公告基本信息和内容
    controller -->> web : 返回公告基本信息和内容
    web ->> web : 展示公告基本信息
    web ->> web : 解析并渲染HTML形式的公告内容
    alt  如果解析HTML时，发现公告内容涉及图片
        web ->> controller : 给定 file_id 获取图片
        controller ->> f_service : 给定 file_id，获取图片 URL
        f_service ->> storage : 获取图片 URL
        storage -->> f_service : 返回图片 URL
        f_service -->> controller : 返回图片 URL
        controller -->> web : 返回重定向 URL
        web ->> storage : 依据重定向的获取图片
        storage -->> web : 返回图片
    end
```
