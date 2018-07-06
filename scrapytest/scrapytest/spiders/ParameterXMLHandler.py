from xml.etree.ElementTree import ElementTree, Element

from MainXMLHandler import MainXMLHandler

class ParameterXMLHandler():

    def __init__(self, ParameterXML_path):
        self.tree = self.read_xml(ParameterXML_path)

    def read_xml(self, ParameterXML_path):
        tree = ElementTree()
        tree.parse(ParameterXML_path)
        return tree

    def find_nodes(self, path):
        return self.tree.findall(path)

    def if_match(self, node, kv_map):
        for key in kv_map:
            if node.get(key) != kv_map.get(key):
                return False
        return True

    def get_node_by_keyvalue(self, path, kv_map):
        nodelist = self.find_nodes(path)
        for node in nodelist:
            if self.if_match(node, kv_map):
                return node
        print('not find')
        return

    def find_children_node(self, node, path):
        return node.findall(path)


if __name__ == "__main__":

    MainXMLHandler = MainXMLHandler('./SpiderControl.xml')
    PageNodes = MainXMLHandler.find_nodes('PageFiles/PageFile')

    page_file = []
    for Node in PageNodes:
        page_file += Node.findall('FilePath')

    for file in page_file:
        print(file.text)
        if file.text != None:
            ParaXMLHandler = ParameterXMLHandler(file.text)


            Frequency = ParaXMLHandler.find_nodes('Frequency')
            print(type(Frequency[0].text))

            PageUrl = ParaXMLHandler.find_nodes('PageUrl')
            print(PageUrl[0].text)

            PageAllowDomains = ParaXMLHandler.find_nodes('PageAllowDomains')
            print(PageAllowDomains[0].text)

            PageAllowDomainsRE = ParaXMLHandler.find_nodes('PageAllowDomainsRE')
            print(PageAllowDomainsRE[0].text)

            PageStartUrl = ParaXMLHandler.find_nodes('PageStartUrl')
            print(PageStartUrl[0].text)

            PageRule_allow = ParaXMLHandler.find_nodes('PageRule_allow')
            print(PageRule_allow[0].text)

            BrowserDriverPath = ParaXMLHandler.find_nodes('BrowserDriverPath')
            print(BrowserDriverPath[0].text)

            ContentUrl = ParaXMLHandler.find_nodes('XPath/TitlePage/ContentUrl')
            print(ContentUrl[0].text)

            NextPageUrl = ParaXMLHandler.find_nodes('XPath/TitlePage/NextPage')
            print(NextPageUrl[0].text)

            ContentUrl = ParaXMLHandler.find_nodes('XPath/TitlePage/ContentUrl')
            print(ContentUrl[0].text)

            project_name = ParaXMLHandler.find_nodes('XPath/ContentPage/project_name')
            print(project_name[0].text)

            project_category = ParaXMLHandler.find_nodes('XPath/ContentPage/project_category')
            print(project_category[0].text)

            project_apartment = ParaXMLHandler.find_nodes('XPath/ContentPage/project_apartment')
            print(project_apartment[0].text)

            project_location = ParaXMLHandler.find_nodes('XPath/ContentPage/project_location')
            print(project_location[0].text)

            project_publish_time = ParaXMLHandler.find_nodes('XPath/ContentPage/project_publish_time')
            print(project_publish_time[0].text)

            project_file_time = ParaXMLHandler.find_nodes('XPath/ContentPage/project_file_time')
            print(project_file_time[0].text)

            project_file_price = ParaXMLHandler.find_nodes('XPath/ContentPage/project_file_price')
            print(project_file_price[0].text)

            project_bit_time = ParaXMLHandler.find_nodes('XPath/ContentPage/project_bit_time')
            print(project_bit_time[0].text)

            project_bit_location = ParaXMLHandler.find_nodes('XPath/ContentPage/project_bit_location')
            print(project_bit_location[0].text)

            project_except_amount = ParaXMLHandler.find_nodes('XPath/ContentPage/project_except_amount')
            print(project_except_amount[0].text)

            project_manager = ParaXMLHandler.find_nodes('XPath/ContentPage/project_manager')
            print(project_manager[0].text)

            project_manager_tel = ParaXMLHandler.find_nodes('XPath/ContentPage/project_manager_tel')
            print(project_manager_tel[0].text)





