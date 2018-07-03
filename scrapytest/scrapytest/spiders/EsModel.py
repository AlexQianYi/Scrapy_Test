# Word type
from elasticsearch_dsl import DocType, Completion, Keyword, Text, Boolean, Integer, Date

from elasticsearch_dsl.connections import connections

from elasticsearch_dsl.analysis import CustomAnalyzer

# Step 1 Create ES connection
connections.create_connection(hosts = ["127.0.0.1"])

# define word divider
class Analyzer(CustomAnalyzer):

    def get_analysis_definition(self):
        return {}

# create word analysis object
ik_analyzer = Analyzer('ik_max_word', filter = ['lowercase'])

class Field(DocType):
    # Auto Complete in search
    suggest = Completion(analyzer=ik_analyzer)

