# Word type
#from elasticsearch_dsl import DocType, Completion, Keyword, Text, Boolean, Integer, \
#    Date, Nested, analyzer, InnerDoc

from elasticsearch_dsl import Document, Completion, Keyword, Text, Boolean, Integer, \
    Date, Nested, analyzer, InnerDoc

from elasticsearch_dsl.connections import connections

from elasticsearch_dsl.analysis import CustomAnalyzer

from datetime import datetime

# Step 1 Create ES connection
connections.create_connection(hosts = ["localhost"])

class LagouType(Document):

    project_url = Keyword()

    project_name = Text(analyzer = 'snowball')

    project_category = Text(analyzer = 'snowball')

    project_apartment = Keyword()

    project_location = Keyword()

    project_publish_time = Date()

    project_file_time = Date()

    project_file_price = Keyword()

    project_file_location = Keyword()

    project_bit_time = Date()

    project_bit_location = Keyword()

    project_except_amount = Keyword()

    project_manager = Keyword()

    project_manger_tel = Keyword()

    #crawl_time = Date()

    class Index:

        name = 'lagou'

    """
    def save(self, ** kwargs):
        self.lines = len(self.body.split())
        return super(Lagou, self).save(** kwargs)

    def is_published(self):
        return datetime.now() > self.published_from
    """

if __name__ == '__main__':
    LagouType.init()


