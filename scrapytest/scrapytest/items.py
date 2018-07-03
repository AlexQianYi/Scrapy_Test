# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class ScrapytestItem(scrapy.Item):
    # define the fields for your item here like:

    # main page
    title = scrapy.Field()
    url = scrapy.Field()
    content_link = scrapy.Field()
    publish_time = scrapy.Field()
    location = scrapy.Field()
    people = scrapy.Field()

    # detail page
    url_object_id = scrapy.Field()

    project_url = scrapy.Field()
    project_name = scrapy.Field()
    project_category = scrapy.Field()
    project_apartment = scrapy.Field()
    project_location = scrapy.Field()
    project_publish_time = scrapy.Field()
    project_file_time = scrapy.Field()
    project_file_price = scrapy.Field()
    project_file_location = scrapy.Field()
    project_bit_time = scrapy.Field()
    project_bit_location = scrapy.Field()
    project_except_amount = scrapy.Field()
    project_manager = scrapy.Field()
    project_manager_tel = scrapy.Field()

    crawl_time = scrapy.Field()

    # pass

def save2es(self):

    lagou_type = LagouType()
    lagou_type.project_url = self['project_url']
    lagou_type.project_name = self['project_name']
    lagou_type.project_category = self['project_category']
    lagou_type.project_apartment = self['project_apartment']
    lagou_type.project_location = self['project_location']
    lagou_type.project_publish_time = self['project_publish_time']
    lagou_type.project_file_time = self['project_file_time']
    lagou_type.project_file_price = self['project_file_price']
    lagou_type.project_file_location = self['project_file_location']
    lagou_type.project_bit_time = self['project_bit_time']
    lagou_type.project_bit_location = self['project_bit_location']
    lagou_type.project_except_amount = self['project_except_amount']
    lagou_type.project_manager = self['project_manager']
    lagou_type.project_maager = self['project_manager_tel']

    lagou_type.crawl_time = self['crawl_time']

    lagou_type.meta.id = self['url_object_id']

    lagou_type.save()

    return


