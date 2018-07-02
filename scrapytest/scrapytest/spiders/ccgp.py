# -*- coding: utf-8 -*-
import scrapy
from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule

from scrapytest.items import ScrapytestItem

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

    def parse(self, response):

        item = ScrapytestItem()

        item['url'] = [response.url]
        print('aaa')

        #self.logger.debug('start to parse url: %s' % response.url)

        if re.search(r'ccgp\.gov\.cn', response.url) is not None:
            return self.parse_item_ccgp(response)

        return item

    def parse_item_ccgp(self, response):

        item = ScrapytestItem()
        item['url'] = [response.url]

        item['publish_time'] = \
            response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/em[1]/text()').extract()

        item['location'] = \
            response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/em[2]/text()').extract()

        item['people'] = \
            response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/em[3]/text()').extract()

        # content title
        item['title'] = \
            response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/a/text()').extract()

        # content link
        item['content_link'] = \
            response.xpath('//*[@id="detail"]/div[2]/div/div[1]/div/div[2]/div[1]/ul/li/a[contains(@target, "_blank")]/@href').extract()


        for key in item:
            for data in item[key]:
                self.logger.debug('item %s value %s' % (key, data))
                #print('item %s value %s' % (key, data))

        return item

        def parse_item_ccgp_next(self, response):
            

    #/ *[ @ id = "detail"] / div[2] / div / div[1] / div / div[2] / div[1] / ul / li[1] / a[
        #contains( @ target, '_blank')] / @href
