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
from .ParameterXMLHandler import ParameterXMLHandler



class CcgpSpider(CrawlSpider):
    name = 'ccgp'

    page_file = []

    Frequency_list = []
    PageUrl_list = []
    allowed_domains = []
    PageAllowDomainsRE_list = []
    start_urls = []
    PageRule_allow_list = []

    ContentUrl_list = []
    NextPage_list = []
    ContentUrlFile_list = []

    project_name_list = []
    project_category_list = []
    project_apartment_list = []
    project_location_list = []
    project_publish_time_list = []
    project_file_time_list = []
    project_file_price_list = []
    project_file_location_list= []
    project_bit_time_list = []
    project_bit_location_list= []
    project_except_amount_list = []
    project_manager_list = []
    project_manager_tel_list = []

    rules = []

    XML_file_path = []


    def __init__(self):
        self.Global_Index = 0
        self.Global_Index_max = 0
        self.ReadXML('./SpiderControl.xml')
        self.driver = webdriver.Firefox(executable_path='/Users/yiqian/Downloads/geckodriver')


        self.allowed_domains = ['ccgp.gov.cn']
        self.start_urls = ['http://www.ccgp.gov.cn/cggg/zygg/gkzb/']

        self.url_set = set()


    def parse(self, response):

        item = ScrapytestItem()

        item['url'] = [response.url]

        for i in range(self.Global_Index_max):
            if re.search(self.PageAllowDomainsRE_list[i], response.url) is not None:
                return self.parse_single_page_ccgp(response, i)

        return item


    def parse_single_page_ccgp(self, response, index):

        self.driver.get(response.url)
        count = 1
        while True:
            wait = WebDriverWait(self.driver, 10)
            print(self.ContentUrl_list[index])
            print(self.NextPage_list[index])
            wait.until(lambda driver: driver.find_element_by_xpath(self.ContentUrl_list[index]))
            sel_list = self.driver.find_elements_by_xpath(self.ContentUrl_list[index])
            IncompleteUrl_list = [sel.get_attribute('href') for sel in sel_list]
            CompleteUrl_list = [response.urljoin(link) for link in IncompleteUrl_list]
            self.url_set |= set(CompleteUrl_list)

            try:
                wait = WebDriverWait(self.driver, 10)
                wait.until(lambda driver: driver.find_element_by_xpath(self.NextPage_list[index]))
                next_page = self.driver.find_element_by_xpath(self.NextPage_list[index])
                next_page.click()
                count += 1
            except:
                print('get to the last page')
                self.driver.close()
                break

        with open('url_set.txt', mode = 'w') as f:
            f.write(repr(self.url_set))

        for link in self.url_set:
            self.Global_Index = index
            yield scrapy.Request(link, callback = self.parse_item_ccgp_content)

    def parse_item_ccgp_content(self, response):

        print('parse item url: ' + response.url)
        item = ScrapytestItem()

        item['project_url'] = response.url

        item['project_name'] = response.xpath(self.project_name_list[self.Global_Index]).extract()

        item['project_category'] = response.xpath(self.project_category_list[self.Global_Index]).extract()

        item['project_apartment'] = response.xpath(self.project_apartment_list[self.Global_Index]).extract()

        item['project_location'] = response.xpath(self.project_location_list[self.Global_Index]).extract()

        item['project_publish_time'] = response.xpath(self.project_publish_time_list[self.Global_Index]).extract()

        item['project_file_time'] = response.xpath(self.project_file_time_list[self.Global_Index]).extract()

        item['project_file_price'] = response.xpath(self.project_file_price_list[self.Global_Index]).extract()

        item['project_file_location'] = response.xpath(self.project_file_location_list[self.Global_Index]).extract()

        item['project_bit_time'] = response.xpath(self.project_bit_time_list[self.Global_Index]).extract()

        item['project_bit_location'] = response.xpath(self.project_bit_location_list[self.Global_Index]).extract()

        item['project_except_amount'] = response.xpath(self.project_except_amount_list[self.Global_Index]).extract()

        item['project_manager'] = response.xpath(self.project_manager_list[self.Global_Index]).extract()

        item['project_manager_tel'] = response.xpath(self.project_manager_tel_list[self.Global_Index]).extract()

        for key in item:
            for data in item[key]:
                self.logger.debug('item %s value %s' % (key, data))

        yield item

    def ReadXML(self, MainXML_path):

        Config_file_list = self.ReadMainXML(MainXML_path)

        for file in Config_file_list:
            if file.text != None:
                self.ReadConfigXML(file)
                self.Global_Index_max += 1

        self.initRules()

    def ReadMainXML(self, MainXML_path):

        XMLHandler = MainXMLHandler('./SpiderControl.xml')
        PageNodes = XMLHandler.find_nodes('PageFiles/PageFile')

        Config_file_list = []
        for Node in PageNodes:
            Config_file_list += Node.findall('FilePath')

        return Config_file_list

    def ReadConfigXML(self, Config_file):

        ParaXMLHandler = ParameterXMLHandler(Config_file.text)

        Frequency = ParaXMLHandler.find_nodes('Frequency')
        self.Frequency_list.append(int(Frequency[0].text))

        PageUrl = ParaXMLHandler.find_nodes('PageUrl')
        self.PageUrl_list.append(PageUrl[0].text)

        PageAllowDomains = ParaXMLHandler.find_nodes('PageAllowDomains')
        self.allowed_domains.append(PageAllowDomains[0].text)

        PageAllowDomainsRE = ParaXMLHandler.find_nodes('PageAllowDomainsRE')
        self.PageAllowDomainsRE_list.append(PageAllowDomainsRE[0].text)

        PageStartUrl = ParaXMLHandler.find_nodes('PageStartUrl')
        self.start_urls.append(PageStartUrl[0].text)

        PageRule_allow = ParaXMLHandler.find_nodes('PageRule_allow')
        self.PageRule_allow_list.append(PageRule_allow[0].text)

        ContentUrl = ParaXMLHandler.find_nodes('XPath/TitlePage/ContentUrl')
        self.ContentUrl_list.append(ContentUrl[0].text)

        NextPageUrl = ParaXMLHandler.find_nodes('XPath/TitlePage/NextPage')
        self.NextPage_list.append(NextPageUrl[0].text)

        ContentUrl = ParaXMLHandler.find_nodes('XPath/TitlePage/ContentUrl')
        self.ContentUrl_list.append(ContentUrl[0].text)

        project_name = ParaXMLHandler.find_nodes('XPath/ContentPage/project_name')
        self.project_name_list.append(project_name[0].text)

        project_category = ParaXMLHandler.find_nodes('XPath/ContentPage/project_category')
        self.project_category_list.append(project_category[0].text)

        project_apartment = ParaXMLHandler.find_nodes('XPath/ContentPage/project_apartment')
        self.project_apartment_list.append(project_apartment[0].text)

        project_location = ParaXMLHandler.find_nodes('XPath/ContentPage/project_location')
        self.project_location_list.append(project_location[0].text)

        project_publish_time = ParaXMLHandler.find_nodes('XPath/ContentPage/project_publish_time')
        self.project_publish_time_list.append(project_publish_time[0].text)

        project_file_time = ParaXMLHandler.find_nodes('XPath/ContentPage/project_file_time')
        self.project_file_time_list.append(project_file_time[0].text)

        project_file_price = ParaXMLHandler.find_nodes('XPath/ContentPage/project_file_price')
        self.project_file_price_list.append(project_file_price[0].text)

        project_file_location = ParaXMLHandler.find_nodes('XPath/ContentPage/project_file_location')
        self.project_file_location_list.append(project_file_location[0])

        project_bit_time = ParaXMLHandler.find_nodes('XPath/ContentPage/project_bit_time')
        self.project_bit_time_list.append(project_bit_time[0].text)

        project_bit_location = ParaXMLHandler.find_nodes('XPath/ContentPage/project_bit_location')
        self.project_bit_location_list.append(project_bit_location[0].text)

        project_except_amount = ParaXMLHandler.find_nodes('XPath/ContentPage/project_except_amount')
        self.project_except_amount_list.append(project_except_amount[0].text)

        project_manager = ParaXMLHandler.find_nodes('XPath/ContentPage/project_manager')
        self.project_manager_list.append(project_manager[0].text)

        project_manager_tel = ParaXMLHandler.find_nodes('XPath/ContentPage/project_manager_tel')
        self.project_manager_tel_list.append(project_manager_tel[0].text)

    def initRules(self):
        for i in range(self.Global_Index_max):
            self.rules.append(Rule(LinkExtractor(allow = self.PageRule_allow_list[i]),
             callback='parse_item', follow=False))




