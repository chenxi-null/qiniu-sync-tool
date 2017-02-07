# 七牛文件同步工具

## 背景
使用hexo搭建个人博客。
hexo将markdown文件转成html页面，而后这部分html文件可以被托管到一些提供静态页面托管服务的网站，例如github-pages和coding-pages，十分方便。
最终选择"七牛"作为markdown文件的图床，一是将图片地址和博文地址分开来，将来便于迁移；二是"七牛"作为国内的公共服务提供商没有被墙的风险，综合考虑选择了七牛。
七牛本身提供了qshell这一命令行工具，帮助我们上传文件，管理云端文件等，但是但就上传本地图片至七牛，然后再将图片链接写入markdown文件这一系列操作而言，似乎不是很方便，所以决定写一个文件上传工具，在qshell这一工具的基础上，增加一些个性化需求。


## 使用说明

### 目录结构

>-- program-root
>+ -- conf
>  - -- config.json
>+ -- qnsync.jar

### 配置文件(config.json)

>{
>  "sync-dir": "同步文件夹",
>  "pshell": "pshell.exe位置",
>  "bucket": "七牛的bucket",
>  "domain": "bucket对应的domain"
>}

### 操作步骤
配置一个本地文件目录(既上面提到的”同步文件夹“)，把想要上传的文件丢到里面，程序支持“增量同步功能”，也就是说只上传新增的文件。
把文件丢进指定目录后，使用命令行运行指令，程序自动去调用qshell的命令把文件上传到七牛(所以你本地必须要有pshell.exe，并且配置好了pshell，能够使用pshell访问七牛)，然后把文件的外链写入剪切板。
写博客时引用图片大多是使用的截图，下面以截图然后引入图片为例，梳理一下操作步骤：
+ 第一步：使用截取软件（我用的是qq截图），图片另存为到指定目录。
+ 第二步：运行命令行程序，程序会自动把图片上传到七牛，然后把外链写入系统剪切板
+ 第三步：回到markdown编辑器（如我用的马克飞象）执行ctrl+v 就得到图片的链接了，搞定！

力求最高效地引入图片，提高写作效率。如果大家还有更好的方法欢迎交流。


## 详细设计思路

### 功能模块
1. 增量同步
2. 允许设置文件夹，文件目录结构代表“文件的前缀”，这里需要说明一下，在七牛上每个bucket（也就是存储空间）里是没有文件夹这个概念的，但是可以通过名命名让文件看起来像处于某个目录当中，例如”a/b/c/file.txt" 就是一个文件的名称，这里可以把“a/b/c”当作上文提到的“文件的前缀”，如果本地的file.txt文件是放在{root_dir}/a/b/c目录下的，那么它上传到七牛之后文件名称就会变成“a/b/c/file.txt”。
2. 调用pshell上传文件，并将图片外链写入剪切板

### 实现方式
1. 增量同步
要求区分已经上传过的文件和新增文件，这就需要引入数据持久化功能，来记录曾经上传过的文件。
目前想到的是简单地使用一个文件还存储持久化信息，使用json数组的格式；还可以考虑使用轻量级数据库sqllite。

2. 调用pshell上传文件，并将图片外链写入剪切板

3. 读取配置文件信息
配置文件包含如下信息：
+ 同步文件夹位置
+ pshell.exe所在目录
+ 七牛的bucket名称
+ 当前bucket对应的域名

### 程序运行流程
扫描“同步文件夹“，取得所有文件，和已上传文件进行比对，得到所有新增的文件。
调用pshell，依次将这些新增文件上传至七牛，最后把这些链接都写入系统剪切板。
  
