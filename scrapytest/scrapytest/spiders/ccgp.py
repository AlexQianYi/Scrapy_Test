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

class CcgpSpider(CrawlSpider):
    name = 'ccgp'
    allowed_domains = ['ccgp.gov.cn']
    start_urls = ['http://www.ccgp.gov.cn/cggg/zygg/gkzb/']

    rules = [
        Rule(LinkExtractor(allow = 'www.ccgp.gov.cn/cggg/zyggg/gkzb/201807/t.*?htm'),
             callback='parse_item', follow=False)
    ]

    def __init__(self):

        self.driver = webdriver.Firefox(executable_path='/Users/yiqian/Downloads/geckodriver')
        self.url_set = set()

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

        ## handle single page content

        #item = ScrapytestItem()
        #item['url'] = [response.url]

        #item['publish_time'] = \
        #    response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/em[1]/text()').extract()

        #item['location'] = \
        #    response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/em[2]/text()').extract()

        #item['people'] = \
        #    response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/em[3]/text()').extract()

        # content title
        #item['title'] = \
        #    response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/a/text()').extract()

        #IncompletetLinks = response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/a[contains(@target, "_blank")]/@href').extract()

        #NextPageLink = response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[2]/p/a[7][contains(@class, "next")]/@href').extract()

        #for link in IncompletetLinks:
        #    link = response.urljoin(link)
        #    yield Request(link, callback=self.parse_item_ccgp_content)

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

