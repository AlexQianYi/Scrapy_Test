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

    # pass
