# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from .EsModel import LagouType

from elasticsearch import Elasticsearch

es = Elasticsearch()

class ScrapytestPipeline(object):
    def process_item(self, item, spider):
        #print(item["name"])
        #print(item["url"])
        #print('title')
        #print(item['title'])

        lagou_type = LagouType()

        lagou_type.project_url = item['project_url']
        lagou_type.project_name = item['project_name']
        lagou_type.project_category = item['project_category']
        lagou_type.project_apartment = item['project_apartment']
        lagou_type.project_location = item['project_location']
        lagou_type.project_publish_time = item['project_publish_time']

        lagou_type.project_file_time = item['project_file_time']
        lagou_type.project_file_price = item['project_file_price']
        lagou_type.project_file_location = item['project_file_location']

        lagou_type.project_bit_time = item['project_bit_time']
        lagou_type.project_bit_location = item['project_bit_location']

        lagou_type.project_except_amount = item['project_except_amount']
        lagou_type.project_manager = item['project_manager']
        lagou_type.project_manager_tel = item['project_manager_tel']

        lagou_type.save()


        #item.save2es()
        return item
