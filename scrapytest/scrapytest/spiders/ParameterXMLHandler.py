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
            print(Frequency[0].text)


