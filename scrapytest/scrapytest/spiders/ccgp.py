# -*- coding: utf-8 -*-
import scrapy
from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
from scrapy import Request

from scrapytest.items import ScrapytestItem

from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait

import sys
import re

import xml.sax

from .MainXMLHandler import MainXMLHandler



class CcgpSpider(CrawlSpider):
    name = 'ccgp'
    allowed_domains = []
    start_urls = []
    page_file = []

    rules = [
        Rule(LinkExtractor(allow = 'www.ccgp.gov.cn/cggg/zyggg/gkzb/201807/t.*?htm'),
             callback='parse_item', follow=False)
    ]

    XML_file_path = []

    def __init__(self):

        self.MainXMLHandler = MainXMLHandler('./SpiderControl.xml')

        self.driver = webdriver.Firefox(executable_path='/Users/yiqian/Downloads/geckodriver')
        self.allowed_domains = ['ccgp.gov.cn']
        self.start_urls = ['http://www.ccgp.gov.cn/cggg/zygg/gkzb/']


        self.ReadMainXML()

        self.url_set = set()

    class MainXMLHandler(xml.sax.ContentHandler):
        def __int__(self):
            self.CurrentData = ""
            self.FilePath = ""
            self.PageFile = ""

        def startElement(self, tag, attributes):
            self.CurrentData = tag
            if tag == "PageFile":
                PageID = attributes["id"]
                print("===========Page " + PageID + "==============")

        def endElement(self, tag):
            if self.CurrentData == "FilePath":
                print ("File path: " + self.FilePath)
            self.CurrentData = ""

        def characters(self, content):
            if self.CurrentData == "PageFile":
                self.PageFile = content
            elif self.CurrentData == "FilePath":
                self.FilePath = content


    def parse(self, response):

        item = ScrapytestItem()

        item['url'] = [response.url]

        if re.search(r'ccgp\.gov\.cn', response.url) is not None:
            return self.parse_single_page_ccgp(response)

        return  item

    def parse_single_page_ccgp(self, response):

        self.driver.get(response.url)
        count = 1
        while True:
            wait = WebDriverWait(self.driver, 10)
            wait.until(lambda driver: driver.find_element_by_xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/a'))
            sel_list = self.driver.find_elements_by_xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/a')
            IncompleteUrl_list = [sel.get_attribute('href') for sel in sel_list]
            CompleteUrl_list = [response.urljoin(link) for link in IncompleteUrl_list]
            self.url_set |= set(CompleteUrl_list)

            try:
                wait = WebDriverWait(self.driver, 10)
                wait.until(lambda driver: driver.find_element_by_xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[2]/p/a[7]'))
                next_page = self.driver.find_element_by_xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[2]/p/a[7]')
                next_page.click()
                count += 1
            except:
                print('get to the last page')
                self.driver.close()
                break

        with open('url_set.txt', mode = 'w') as f:
            f.write(repr(self.url_set))

        for link in self.url_set:
            yield scrapy.Request(link, callback = self.parse_item_ccgp_content)

    def parse_item_ccgp_content(self, response):

        print('parse item url: ' + response.url)
        item = ScrapytestItem()

        item['project_url'] = response.url

        item['project_name'] = response.xpath('//*[@class="table"]//tr[2]/td[2]/text()').extract()

        item['project_category'] = response.xpath('//*[@class="table"]//tr[3]/td[2]/p/text()').extract()

        item['project_apartment'] = response.xpath('//*[@class="table"]//tr[4]/td[2]/text()').extract()

        item['project_location'] = response.xpath('//*[@class="table"]//tr[5]/td[2]/text()').extract()

        item['project_publish_time'] = response.xpath('//*[@class="table"]//tr[5]/td[4]/text()').extract()

        item['project_file_time'] = response.xpath('//*[@class="table"]//tr[6]/td[2]/text()').extract()

        item['project_file_price'] = response.xpath('//*[@class="table"]//tr[7]/td[2]/text()').extract()

        item['project_file_location'] = response.xpath('//*[@class="table"]//tr[8]/td[2]/text()').extract()

        item['project_bit_time'] = response.xpath('//*[@class="table"]//tr[9]/td[2]/text()').extract()

        item['project_bit_location'] = response.xpath('//*[@class="table"]//tr[10]/td[2]/text()').extract()

        item['project_except_amount'] = response.xpath('//*[@class="table"]//tr[11]/td[2]/text()').extract()

        item['project_manager'] = response.xpath('//*[@class="table"]//tr[13]/td[2]/text()').extract()

        item['project_manager_tel'] = response.xpath('//*[@class="table"]//tr[14]/td[2]/text()').extract()

        for key in item:
            for data in item[key]:
                self.logger.debug('item %s value %s' % (key, data))

        yield item

    def ReadMainXML(self):

        PageNodes = MainXMLHandler.find_nodes('PagesFiles/PageFile')

        for Node in PageNodes:
            self.page_file.append(Node.findall('FilePath'))

        print


        # node = XMLHandler.find_node('PageFiles/PageFile')
        result = XMLHandler.get_node_by_keyvalue("PageFiles/PageFile", {"id": "1"})
        node = result.findall('FilePath')

        print(result)
        print(node[0], node[0].text)







