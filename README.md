爬虫项目ReadMe

# 1.Python爬虫（Scrapy）
## 1.1 环境

操作系统：macOS 10.13.4

Python版本: 3.6.2

## 1.2 Python module

scrapy

elasticsearch_dsl

selenium

## 2.1 使用
### 2.1.1 主配置文件

主配置文件**SpiderControl.xml**保存了每个爬虫单例的配置文件名，每个爬虫运行间隔时间，爬虫运行的指令。

	<FilePath> 每个爬虫单例的配置文件路径
	<Frequency> 每个爬虫运行时间间隔
	<cmd> 每个爬虫运行的指令 "scrapy crawl" + 爬虫名

### 2.1.2 爬虫配置文件

爬虫配置文件**CCGP\_bit_invitation.xml**保存了爬虫的目标url，起始url，所需要爬取元素的xpath等信息。

	<PageUrl> 网页url
	<PageAllowDomains> 允许的域名
	<PageAllowDomainsRE> 允许域名的正则表达式
	<PageStartUrl> 爬虫起始url
	<PageRule_allow> 爬虫所爬url的范围，通常限定在某个月或者某年
	<ContentUrl> 具体子页面内容的url的xpath
	<NextPage> 下一页按钮的xpath
	<ContentUrlFile> 保存已爬取url的文件名
具体爬取信息的xpath

	<project_name> 项目名
	<project_category> 项目类别
	<project_apartment> 发布项目的部门
	<project_location> 项目地址
	<project_publish_time> 项目发布时间
	<project_file_time> 项目文件的时间
	<project_file_price> 项目文件的价格
	<project_file_location> 项目文件领取地址
	<project_bit_time> 项目竞标时间
	<project_bit_location> 项目竞标地点
	<project_except_amount> 项目预期价格
	<project_manager> 项目负责人
	<project_manager_tel> 项目负责人电话
	
### 2.2 创建新爬虫
	
复制ccgp.py，并修改爬虫类名和爬虫名为test

	name = 'ccgp' -> name = 'test'
复制爬虫配置文件**CCGP\_bit_invitation.xml**，改名为**test.xml**，修改其中的xpath规则
	

在主配置文件**SpiderControl.xml**中添加新的爬虫规则

	<PageFile id = "2">
		<FilePath>./test.xml</FilePath>
		<Frequency>500</Frequency>		间隔500s爬取一次
		<cmd>scrapy crawl test</cmd>
	</PageFile>
	
执行**StartSpider.py**，执行前需启动ElasticSearch

### 2.3 ElasticSearch创建

执行**EsModel.py**

修改ES名
	
	name = 'original_project_file' -> name = 'test_ES'
	
修改存储数据类型名

	doc_type = 'project_file' -> doc_type = 'doc'

