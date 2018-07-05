from xml.etree.ElementTree import ElementTree, Element

class MainXMLHandler():

    def __init__(self, MainXML_path):
        self.tree = self.read_xml(MainXML_path)

    def read_xml(self, MainXML_path):
        tree = ElementTree()
        tree.parse(MainXML_path)
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


if __name__ == "__main__":

    XMLHandler = MainXMLHandler("./SpiderControl.xml")

    #node = XMLHandler.find_node('PageFiles/PageFile')
    result = XMLHandler.get_node_by_keyvalue("PageFiles/PageFile", {"id":"1"})
    node = result.findall('FilePath')

    print(result)
    print(node[0], node[0].text)


"""
class MainXMLHandler(xml.sax.ContentHandler):
    def __int__(self):
        self.CurrentData = ""
        self.FilePath = ""
        self.PageFile = ""

    def startElement(self, tag, attributes):
        self.CurrentData = tag
        if tag == "PageFile":
            PageID = attributes["id"]
            print("===========Page " + PageID + "==============")

    def endElement(self, tag):
        if self.CurrentData == "FilePath":
            print ("File path: " + self.FilePath)
        self.CurrentData = ""

    def characters(self, content):
        if self.CurrentData == "PageFile":
            self.PageFile = content
        elif self.CurrentData == "FilePath":
            self.FilePath = content



if __name__ == "__main__":

    parser = xml.sax.make_parser()

    parser.setFeature(xml.sax.handler.feature_namespaces, 0)

    Handler = MainXMLHandler()
    parser.setContentHandler(Handler)

    parser.parse("./SpiderControl.xml")
"""
